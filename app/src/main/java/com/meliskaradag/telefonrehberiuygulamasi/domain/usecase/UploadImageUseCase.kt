package com.meliskaradag.telefonrehberiuygulamasi.domain.usecase

import com.meliskaradag.telefonrehberiuygulamasi.domain.repository.ContactRepository
import java.io.File

class UploadImageUseCase(private val repo: ContactRepository) {
    suspend operator fun invoke(file: File): String? = repo.uploadImage(file)
}