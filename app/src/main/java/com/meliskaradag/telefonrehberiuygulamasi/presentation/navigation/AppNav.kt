package com.meliskaradag.telefonrehberiuygulamasi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.meliskaradag.telefonrehberiuygulamasi.presentation.contacts.ContactsScreen
import com.meliskaradag.telefonrehberiuygulamasi.presentation.addedit.AddEditScreen
import com.meliskaradag.telefonrehberiuygulamasi.presentation.addedit.saveToDeviceContacts

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = nav, startDestination = "contacts") {
        composable("contacts") {
            ContactsScreen(
                onAddNew = { nav.navigate("addedit") },
                onEdit = { id -> nav.navigate("addedit?contactId=$id") }
            )
        }

        composable(
            route = "addedit?contactId={contactId}",
            arguments = listOf(navArgument("contactId") {
                type = NavType.StringType; nullable = true; defaultValue = null
            })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")
            AddEditScreen(
                isEdit = contactId != null,
                contactId = contactId,
                onCancel = { nav.popBackStack() },
                onSaveToDevice = { first, last, phone ->
                    context.saveToDeviceContacts(first, last, phone)
                }
            )
        }
    }
}
