package com.meliskaradag.telefonrehberiuygulamasi.domain.usecase

import com.meliskaradag.telefonrehberiuygulamasi.domain.repository.ContactRepository

class RefreshContactsUseCase(private val repo: ContactRepository) {
    suspend operator fun invoke() = repo.refreshContactsFromApi()
}