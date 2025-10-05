package com.meliskaradag.telefonrehberiuygulamasi.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.meliskaradag.telefonrehberiuygulamasi.presentation.contacts.ContactsScreen
import com.meliskaradag.telefonrehberiuygulamasi.presentation.addedit.AddEditScreen

@Composable
fun AppNav() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = NavRoutes.Contacts) {
        composable(NavRoutes.Contacts) {
            ContactsScreen(
                onAddNew = { nav.navigate(NavRoutes.AddEdit()) },
                onEdit = { id -> nav.navigate(NavRoutes.AddEdit(id)) }
            )
        }
        composable(
            route = "addedit?contactId={contactId}",
            arguments = listOf(navArgument("contactId") {
                type = NavType.StringType; nullable = true; defaultValue = null
            })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")
            AddEditScreen(contactId = contactId, onDone = { nav.popBackStack() })
        }
    }
}