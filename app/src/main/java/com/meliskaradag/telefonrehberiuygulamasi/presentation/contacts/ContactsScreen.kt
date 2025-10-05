package com.meliskaradag.telefonrehberiuygulamasi.presentation.contacts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.meliskaradag.telefonrehberiuygulamasi.domain.model.Contact

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    onAddNew: () -> Unit,
    onEdit: (String) -> Unit,
    vm: ContactsViewModel = viewModel()
) {
    val state by vm.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNew) { Icon(Icons.Default.Add, contentDescription = "Yeni") }
        }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize()) {
            // Arama kutusu + recent queries dropdown
            var tf by remember { mutableStateOf(TextFieldValue(state.query)) }
            var showRecent by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = tf,
                onValueChange = {
                    tf = it; vm.onEvent(ContactsEvent.OnQueryChange(it.text))
                    showRecent = true
                },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                label = { Text("İsim/Telefon ara") },
                singleLine = true
            )
            DropdownMenu(expanded = showRecent && state.recentQueries.isNotEmpty(),
                onDismissRequest = { showRecent = false }) {
                state.recentQueries.forEach { q ->
                    DropdownMenuItem(text = { Text(q) }, onClick = {
                        tf = TextFieldValue(q)
                        vm.onEvent(ContactsEvent.OnQueryChange(q))
                        vm.onEvent(ContactsEvent.OnSearch); showRecent = false
                    })
                }
            }

            // Liste
            if (state.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())

            LazyColumn(Modifier.fillMaxSize()) {
                state.grouped.toSortedMap().forEach { (letter, itemsForLetter) ->
                    stickyHeader {
                        Surface(tonalElevation = 3.dp, modifier = Modifier.fillMaxWidth()) {
                            Text(letter.toString(), style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                        }
                    }
                    items(itemsForLetter, key = { it.id }) { c ->
                        ContactRow(
                            contact = c,
                            onClick = { onEdit(c.id) },
                            onDelete = { vm.onEvent(ContactsEvent.OnDelete(c.id)) }
                        )
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactRow(
    contact: Contact,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    // Material3 SwipeToDismissBox
    var dismissState = rememberSwipeToDismissBoxState()

    if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
        onDelete(); // sola kaydırınca silmesi için
        LaunchedEffect(Unit) { dismissState.reset() }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Row(
                Modifier.fillMaxSize().background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) { Text("Sil", color = MaterialTheme.colorScheme.onErrorContainer) }
        },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("${contact.firstName} ${contact.lastName}",
                        style = MaterialTheme.typography.titleMedium)
                    Text(contact.phone, style = MaterialTheme.typography.bodyMedium)
                }
                if (contact.isInDeviceContacts) { // cihaz rehberinde ise ikon göstermesi için
                    AssistChip(onClick = {}, label = { Text("Rehber") })
                }
            }
        }
    )
}