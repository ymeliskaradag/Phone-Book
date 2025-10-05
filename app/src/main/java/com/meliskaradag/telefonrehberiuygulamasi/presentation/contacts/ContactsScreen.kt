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
import androidx.compose.ui.res.painterResource
import com.meliskaradag.telefonrehberiuygulamasi.R

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    onAddNew: () -> Unit,
    onEdit: (String) -> Unit,
    vm: ContactsViewModel = viewModel()
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                containerColor = BrandBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) { Icon(painterResource(R.drawable.ic_add), contentDescription = "Add") }
        },
        topBar = {
            SmallTopAppBar(
                title = { Text("Contacts", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 16.dp)) {
            SearchField(value = state.query, onChange = { vm.onEvent(ContactsEvent.OnQuery(it)) })
            Spacer(Modifier.height(12.dp))

            if (state.items.isEmpty()) {
                EmptyState(onCreate = onAdd)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.grouped.forEach { (letter, contacts) ->
                        stickyHeader {
                            Text(
                                letter.toString(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Background)
                                    .padding(vertical = 8.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = Gray400
                            )
                        }
                        items(contacts, key = { it.id }) { c ->
                            Surface(shape = MaterialTheme.shapes.medium, tonalElevation = 0.dp, shadowElevation = 0.dp, color = SurfaceColor) {
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactRow(
    contact: Contact,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    //Material3 SwipeToDismissBox
    val dismissState = rememberSwipeToDismissBoxState()

    if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
        onDelete() //sola kaydırınca silmek için
        LaunchedEffect(Unit) { dismissState.reset() }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Row(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text("Sil", color = MaterialTheme.colorScheme.onErrorContainer)
            }
        },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        "${contact.firstName} ${contact.lastName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(contact.phone, style = MaterialTheme.typography.bodyMedium)
                }

                //Cihaz rehberinde kayıtlı ise ikon göstermek için
                if (contact.isInDeviceContacts) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_in_device),
                        contentDescription = "Cihaz rehberinde kayıtlı",
                        modifier = Modifier.size(18.dp),
                        //dimen kullanmak için:
                        /*modifier = Modifier.size(
                            // dimen varsa kullan, yoksa 18.dp ile değiştir
                            dimensionResource(id = R.dimen.icon_s)
                        ),*/
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}
