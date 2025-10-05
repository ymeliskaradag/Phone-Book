package com.meliskaradag.telefonrehberiuygulamasi.presentation.addedit

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddEditVm : ViewModel() {
    var localPhotoUri: Uri? by mutableStateOf(null)
        private set

    fun setLocalPhoto(uri: Uri?) {
        localPhotoUri = uri
    }

    fun clearLocalPhoto() {
        localPhotoUri = null
    }
}