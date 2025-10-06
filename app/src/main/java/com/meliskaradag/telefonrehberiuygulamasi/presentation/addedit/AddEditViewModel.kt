package com.meliskaradag.telefonrehberiuygulamasi.presentation.addedit

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.meliskaradag.telefonrehberiuygulamasi.di.ServiceLocator
import com.meliskaradag.telefonrehberiuygulamasi.domain.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class AddEditViewModel(app: Application) : AndroidViewModel(app) {

    private val use = ServiceLocator.provideUseCases(app)

    var state = androidx.compose.runtime.mutableStateOf(AddEditUiState())
        private set

    var localPhotoUri: Uri? = null
        private set

    fun setLocalPhoto(uri: Uri?) { localPhotoUri = uri }
    fun clearLocalPhoto() { localPhotoUri = null }

    //Edit akışında mevcut veriyi göstermek için
    fun loadIfEdit(contactId: String?) {
        if (contactId == null) return
        state.value = state.value.copy(contactId = contactId)

        viewModelScope.launch {
            use.get().collect { list ->
                list.find { it.id == contactId }?.let { c ->
                    state.value = state.value.copy(
                        firstName = c.firstName,
                        lastName = c.lastName,
                        phone = c.phone,
                        photoUrl = c.photoUrl
                    )
                    //Daha önce seçilmiş geçici foto varsa sıfırlamak için
                    clearLocalPhoto()
                }
            }
        }
    }

    fun onChangeFirst(v: String) { state.value = state.value.copy(firstName = v) }
    fun onChangeLast(v: String)  { state.value = state.value.copy(lastName = v) }
    fun onChangePhone(v: String) { state.value = state.value.copy(phone = v) }

    fun onSave() = viewModelScope.launch {
        try {
            state.value = state.value.copy(isLoading = true, error = null)

            //Yerel URI varsa upload etmek ve dönen URL'i almak için
            val uploadedUrl: String? = localPhotoUri?.let { uri ->
                val tempFile = copyUriToTempFile(uri)
                use.uploadImage(tempFile)
            }

            //Domain modele çevirmek için
            val contact = Contact(
                id = state.value.contactId ?: java.util.UUID.randomUUID().toString(),
                firstName = state.value.firstName,
                lastName = state.value.lastName,
                phone = state.value.phone,
                photoUrl = uploadedUrl ?: state.value.photoUrl,
                isInDeviceContacts = false
            )

            //Create/Update
            if (state.value.contactId == null) use.add(contact) else use.update(contact)

            //Temizlemek ve yüklemek için
            clearLocalPhoto()
            state.value = state.value.copy(isLoading = false, saved = true)

        } catch (t: Throwable) {
            state.value = state.value.copy(isLoading = false, error = "Could not be saved")
        }
    }

    fun onDelete() = viewModelScope.launch {
        val id = state.value.contactId ?: return@launch
        try {
            use.delete(id)
            state.value = state.value.copy(saved = true)
        } catch (_: Throwable) {
            state.value = state.value.copy(error = "Could not be deleted")
        }
    }

    //geçici dosya (cache) kopyalar, upload için file gerekir
    private suspend fun copyUriToTempFile(uri: Uri): File = withContext(Dispatchers.IO) {
        val cr = getApplication<Application>().contentResolver
        val input = cr.openInputStream(uri) ?: throw IOException("Photo could not be opened")
        val outFile = File.createTempFile("upload_", ".jpg", getApplication<Application>().cacheDir)
        input.use { ins ->
            outFile.outputStream().use { outs ->
                ins.copyTo(outs)
            }
        }
        outFile
    }
}