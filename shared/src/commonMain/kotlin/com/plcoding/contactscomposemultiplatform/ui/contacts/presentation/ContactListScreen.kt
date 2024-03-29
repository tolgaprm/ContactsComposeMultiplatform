package com.plcoding.contactscomposemultiplatform.ui.contacts.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plcoding.contactscomposemultiplatform.core.presentation.ImagePicker
import com.plcoding.contactscomposemultiplatform.ui.contacts.domain.Contact
import com.plcoding.contactscomposemultiplatform.ui.contacts.presentation.components.AddContactSheet
import com.plcoding.contactscomposemultiplatform.ui.contacts.presentation.components.ContactDetailSheet
import com.plcoding.contactscomposemultiplatform.ui.contacts.presentation.components.ContactListItem
import com.plcoding.contactscomposemultiplatform.ui.contacts.presentation.components.RecentlyAddedContactSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    state: ContactListState,
    newContact: Contact?,
    modifier: Modifier = Modifier,
    imagePicker: ImagePicker,
    onEvent: (ContactListEvent) -> Unit
) {
    imagePicker.registerPicker { imageBytes ->
        onEvent(ContactListEvent.OnPhotoPicked(imageBytes))
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(ContactListEvent.OnAddNewContactClick) },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.PersonAdd,
                    contentDescription = "Add new contact"
                )
            }
        }
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                RecentlyAddedContactSection(
                    contacts = state.recentlyAddedContacts,
                    onClick = {
                        onEvent(ContactListEvent.SelectContact(it))
                    }
                )
            }

            item {
                Text(
                    text = "My Contacts (${state.contacts.size})",
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            items(state.contacts) { contact ->
                ContactListItem(
                    contact = contact,
                    modifier = Modifier.fillMaxWidth()
                        .clickable { onEvent(ContactListEvent.SelectContact(contact)) }
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }

    ContactDetailSheet(
        isOpen = state.isSelectedContactSheetOpen,
        selectedContact = state.selectedContact,
        onEvent = onEvent
    )

    AddContactSheet(
        state = state,
        newContact = newContact,
        isOpen = state.isAddContactSheetOpen,
        onEvent = { event ->
            if (event is ContactListEvent.OnAddPhotoClick) {
                imagePicker.pickImage()
            }
            onEvent(event)
        }
    )
}