package com.meliskaradag.telefonrehberiuygulamasi.domain.usecase

import com.meliskaradag.telefonrehberiuygulamasi.domain.repository.ContactRepository

class DeleteContactUseCase(private val repo: ContactRepository) {
    suspend operator fun invoke(id: String) = repo.delete(id)
}
