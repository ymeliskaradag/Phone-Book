package com.meliskaradag.telefonrehberiuygulamasi.presentation.contacts

sealed interface ContactsEvent {
    data class OnQueryChange(val value: String) : ContactsEvent
    data object OnSearch : ContactsEvent
    data class OnDelete(val id: String) : ContactsEvent
    data object OnRefresh : ContactsEvent
}