package com.plcoding.contactscomposemultiplatform.ui.contacts.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.plcoding.contactscomposemultiplatform.core.presentation.BottomSheetFromWish
import com.plcoding.contactscomposemultiplatform.ui.contacts.domain.Contact
import com.plcoding.contactscomposemultiplatform.ui.contacts.presentation.ContactListEvent
import com.plcoding.contactscomposemultiplatform.ui.contacts.presentation.ContactListState

@Composable
fun AddContactSheet(
    state: ContactListState,
    newContact: Contact?,
    isOpen: Boolean,
    onEvent: (ContactListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheetFromWish(
        visible = isOpen,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                TopSection(
                    newContact = newContact,
                    onAddPhotoClicked = { onEvent(ContactListEvent.OnAddPhotoClick) },
                    onContactPhotoClicked = { onEvent(ContactListEvent.OnAddPhotoClick) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextFieldSection(
                    state = state,
                    newContact = newContact,
                    onFirstNameValueChanged = { onEvent(ContactListEvent.OnFirstNameChanged(it)) },
                    onLastNameValueChanged = { onEvent(ContactListEvent.OnLastNameChanged(it)) },
                    onEmailValueChanged = { onEvent(ContactListEvent.OnEmailChanged(it)) },
                    onPhoneNumberValueChanged = { onEvent(ContactListEvent.OnPhoneNumberChanged(it)) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onEvent(ContactListEvent.SaveContact) }
                ) {
                    Text(text = "Save")
                }
            }

            IconButton(
                onClick = {
                    onEvent(ContactListEvent.DismissContact)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close"
                )
            }
        }
    }
}

@Composable
private fun AddPhotoButton(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .size(150.dp)
            .clip(RoundedCornerShape(40))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onClicked() }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(40)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add Photo",
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(40.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactTextField(
    value: String,
    placeholder: String,
    error: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = placeholder) },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedVisibility(error != null) {
            Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun TopSection(
    newContact: Contact?,
    onAddPhotoClicked: () -> Unit,
    onContactPhotoClicked: () -> Unit
) {
    if (newContact?.photoBytes == null) {
        AddPhotoButton(onClicked = onAddPhotoClicked)
    } else {
        ContactPhoto(
            contact = newContact,
            modifier = Modifier
                .size(150.dp)
                .clickable { onContactPhotoClicked() }
        )
    }
}

@Composable
private fun TextFieldSection(
    state: ContactListState,
    newContact: Contact?,
    onFirstNameValueChanged: (String) -> Unit,
    onLastNameValueChanged: (String) -> Unit,
    onEmailValueChanged: (String) -> Unit,
    onPhoneNumberValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ContactTextField(
            value = newContact?.firstName ?: "",
            placeholder = "First Name",
            error = state.firstNameError,
            onValueChange = onFirstNameValueChanged,
            modifier = Modifier.fillMaxWidth()
        )

        ContactTextField(
            value = newContact?.lastName ?: "",
            placeholder = "Last Name",
            error = state.lastNameError,
            onValueChange = onLastNameValueChanged,
            modifier = Modifier.fillMaxWidth()
        )

        ContactTextField(
            value = newContact?.email ?: "",
            placeholder = "Email",
            error = state.emailError,
            onValueChange = onEmailValueChanged,
            modifier = Modifier.fillMaxWidth()
        )

        ContactTextField(
            value = newContact?.phoneNumber ?: "",
            placeholder = "Phone number",
            error = state.phoneNumberError,
            onValueChange = onPhoneNumberValueChanged,
            modifier = Modifier.fillMaxWidth()
        )
    }
}