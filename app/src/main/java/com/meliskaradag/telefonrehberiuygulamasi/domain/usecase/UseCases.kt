package com.meliskaradag.telefonrehberiuygulamasi.domain.usecase

//ServiceLocator ile ViewModel’lere tek paket halinde vermek için
data class UseCases(
    val get: GetContactsUseCase,
    val refresh: RefreshContactsUseCase,
    val add: AddContactUseCase,
    val update: UpdateContactUseCase,
    val delete: DeleteContactUseCase,
    val search: SearchContactsUseCase,
    val uploadImage: UploadImageUseCase
)