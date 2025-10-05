package com.meliskaradag.telefonrehberiuygulamasi.domain.model

data class Contact(
    val id: String,                  //API/DB kimliği
    val firstName: String,
    val lastName: String,
    val phone: String,
    val photoUrl: String? = null,
    val isInDeviceContacts: Boolean = false //cihaz rehberinde de var mı kontrolü(ikon için)
) {
    val fullName: String get() = "$firstName $lastName".trim()
    val firstLetter: Char get() = fullName.firstOrNull()?.uppercaseChar() ?: '#'
}