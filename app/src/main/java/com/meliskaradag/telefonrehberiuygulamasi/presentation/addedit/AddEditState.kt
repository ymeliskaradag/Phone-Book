package com.meliskaradag.telefonrehberiuygulamasi.presentation.addedit

data class AddEditUiState(
    val contactId: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val photoUrl: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false    // kayıt olunca Lottie göstermek için
)