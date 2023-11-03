package com.plcoding.contactscomposemultiplatform.ui.contacts.data

import com.plcoding.contactscomposemultiplatform.core.data.ImageStorage
import com.plcoding.contactscomposemultiplatform.database.ContactDatabase
import com.plcoding.contactscomposemultiplatform.ui.contacts.domain.Contact
import com.plcoding.contactscomposemultiplatform.ui.contacts.domain.ContactDataSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock

class SqlDelightContactDataSource(
    db: ContactDatabase,
    private val imageStorage: ImageStorage
) : ContactDataSource {
    private val queries = db.contactQueries

    override fun getContacts(): Flow<List<Contact>> {
        return queries.getContacts()
            .asFlow()
            .mapToList()
            .map { contactEntities ->
                supervisorScope {
                    contactEntities.map { contactEntity ->
                        async {
                            contactEntity.toContact(imageStorage)
                        }
                    }.map { it.await() }
                }
            }
    }

    override fun getRecentContacts(amount: Int): Flow<List<Contact>> {
        return queries.getRecentContacts(amount.toLong())
            .asFlow()
            .mapToList()
            .map { contactEntities ->
                supervisorScope {
                    contactEntities.map { contactEntity ->
                        async {
                            contactEntity.toContact(imageStorage)
                        }
                    }.map { it.await() }
                }
            }
    }

    override suspend fun insertContact(contact: Contact) {
        val imagePath = contact.photoBytes?.let {
            imageStorage.saveImage(it)
        }
        queries.insertContactEntity(
            id = contact.id,
            firstName = contact.firstName,
            lastName = contact.lastName,
            email = contact.email,
            phoneNumber = contact.phoneNumber,
            createdAt = Clock.System.now().toEpochMilliseconds(),
            imagePath = imagePath
        )
    }

    override suspend fun deleteContact(id: Long) {
        val entity = queries.getContactById(id).executeAsOne()
        entity.imagePath?.let {
            imageStorage.deleteImage(it)
        }
        queries.deleteContactEntity(id)
    }
}