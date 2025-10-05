package com.meliskaradag.telefonrehberiuygulamasi.presentation.contacts

import com.meliskaradag.telefonrehberiuygulamasi.domain.model.Contact

data class ContactsUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val recentQueries: List<String> = emptyList(),
    val contacts: List<Contact> = emptyList(),
    val grouped: Map<Char, List<Contact>> = emptyMap(),
    val error: String? = null
)
