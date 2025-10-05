package com.meliskaradag.telefonrehberiuygulamasi.domain.repository

import com.meliskaradag.telefonrehberiuygulamasi.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    //Room’dan canlı akış; UI listeleri buradan oluşur
    fun getContactsStream(): Flow<List<Contact>>

    //API’den çekip Room’a yazar
    suspend fun refreshContactsFromApi()

    //CRUD
    suspend fun add(contact: Contact)
    suspend fun update(contact: Contact)
    suspend fun delete(id: String)

    //Boşluk içeren ad-soyad aramalarını da desteklemek için
    suspend fun search(query: String): List<Contact>

    suspend fun uploadImage(file: java.io.File): String?
}
