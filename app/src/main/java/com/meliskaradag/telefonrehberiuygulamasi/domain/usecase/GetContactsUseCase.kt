package com.meliskaradag.telefonrehberiuygulamasi.domain.usecase

import com.meliskaradag.telefonrehberiuygulamasi.domain.model.Contact
import com.meliskaradag.telefonrehberiuygulamasi.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow

class GetContactsUseCase(private val repo: ContactRepository) {
    operator fun invoke(): Flow<List<Contact>> = repo.getContactsStream()
}