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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Silme diyaloğunda gösterilecek hedef kişi (null ise dialog kapalı)
    var deleteTarget by remember { mutableStateOf<Contact?>(null) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                // Yeşil başarı kartı (Figma: #12B76A)
                Snackbar(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(MaterialTheme.shapes.medium),
                    containerColor = SuccessGreen,
                    contentColor = Color.White
                ) {
                    Text(data.visuals.message, style = MaterialTheme.typography.bodyLarge)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNew,
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
                                    onRequestDelete = { deleteTarget = it }
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
    if (deleteTarget != null) {
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            icon = {
                Icon(
                    painterResource(R.drawable.ic_delete),
                    contentDescription = null,
                    tint = ErrorRed
                )
            },
            title = {
                Text(
                    "Delete Contact",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete ${deleteTarget!!.firstName} ${deleteTarget!!.lastName}? This action can’t be undone",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray400   //Figma gri metin
                )
            },
            confirmButton = {
                //Sağdaki kırmızı buton
                Button(
                    onClick = {
                        val id = deleteTarget!!.id
                        deleteTarget = null

                        //VM tarafında sil
                        vm.onEvent(ContactsEvent.OnDelete(id))

                        scope.launch {
                            snackbarHostState.showSnackbar("User is deleted!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorRed,     //#FF0000
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                // Soldaki cancel
                OutlinedButton(
                    onClick = { deleteTarget = null },
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, Gray300),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Gray900,
                        containerColor = Color.Transparent
                    )
                ) {
                    Text("No")
                }
            },
            shape = MaterialTheme.shapes.large,   //Figma’daki yuvarlatma
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContactRow(
    contact: Contact,
    onClick: () -> Unit,
    onRequestDelete: (Contact) -> Unit
) {
    //Material3 SwipeToDismissBox
    val dismissState = rememberSwipeToDismissBoxState()

    if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
        onRequestDelete(contact) //sola kaydırınca silmek için
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
                Text("Delete", color = MaterialTheme.colorScheme.onErrorContainer)
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
