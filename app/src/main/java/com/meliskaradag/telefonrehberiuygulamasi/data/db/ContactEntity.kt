package com.meliskaradag.telefonrehberiuygulamasi.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val photoUrl: String?,
    val isInDeviceContacts: Boolean
)
