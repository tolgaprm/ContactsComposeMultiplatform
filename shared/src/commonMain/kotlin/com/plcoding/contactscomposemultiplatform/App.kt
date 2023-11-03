package com.plcoding.contactscomposemultiplatform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.plcoding.contactscomposemultiplatform.core.presentation.ContactsTheme
import com.plcoding.contactscomposemultiplatform.core.presentation.ImagePicker
import com.plcoding.contactscomposemultiplatform.di.AppModule
import com.plcoding.contactscomposemultiplatform.ui.contacts.presentation.ContactListScreen
import com.plcoding.contactscomposemultiplatform.ui.contacts.presentation.ContactListViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    appModule: AppModule,
    imagePicker: ImagePicker
) {
    val viewModel = getViewModel(
        key = "contactViewModel",
        factory = viewModelFactory {
            ContactListViewModel(
                contactDataSource = appModule.contactDataSource
            )
        }
    )
    val state by viewModel.uiState.collectAsState()
    ContactsTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ContactListScreen(
                state = state,
                newContact = viewModel.newContact,
                onEvent = viewModel::onEvent,
                imagePicker = imagePicker
            )
        }
    }
}