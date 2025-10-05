package com.meliskaradag.telefonrehberiuygulamasi.domain.usecase

import com.meliskaradag.telefonrehberiuygulamasi.domain.model.Contact
import com.meliskaradag.telefonrehberiuygulamasi.domain.repository.ContactRepository

class AddContactUseCase(private val repo: ContactRepository) {
    suspend operator fun invoke(c: Contact) = repo.add(c)
}