package com.meliskaradag.telefonrehberiuygulamasi.domain.usecase

import com.meliskaradag.telefonrehberiuygulamasi.domain.repository.ContactRepository

class SearchContactsUseCase(private val repo: ContactRepository) {
    suspend operator fun invoke(q: String) = repo.search(q)
}