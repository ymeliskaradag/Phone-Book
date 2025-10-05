package com.meliskaradag.telefonrehberiuygulamasi.presentation.navigation

object NavRoutes {
    const val Contacts = "contacts"
    const val AddEdit = "addedit?contactId={contactId}"
    fun AddEdit(contactId: String? = null) =
        if (contactId == null) "addedit" else "addedit?contactId=$contactId"
}