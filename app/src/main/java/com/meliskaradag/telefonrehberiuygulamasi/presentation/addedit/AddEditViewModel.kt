package com.meliskaradag.telefonrehberiuygulamasi.presentation.addedit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.meliskaradag.telefonrehberiuygulamasi.di.ServiceLocator
import com.meliskaradag.telefonrehberiuygulamasi.domain.model.Contact
import kotlinx.coroutines.launch

class AddEditViewModel(app: Application) : AndroidViewModel(app) {
    private val use = ServiceLocator.provideUseCases(app)
    var state = androidx.compose.runtime.mutableStateOf(AddEditUiState())
        private set

    fun loadIfEdit(contactId: String?) {
        if (contactId == null) return
        state.value = state.value.copy(contactId = contactId)
        // eldeki cache'ten bul: listedeki son veriden seçmek yeterli
        viewModelScope.launch {
            use.get().collect { list ->
                list.find { it.id == contactId }?.let { c ->
                    state.value = state.value.copy(
                        firstName = c.firstName, lastName = c.lastName,
                        phone = c.phone, photoUrl = c.photoUrl
                    )
                }
            }
        }
    }

    fun onChangeFirst(v: String) { state.value = state.value.copy(firstName = v) }
    fun onChangeLast(v: String)  { state.value = state.value.copy(lastName = v) }
    fun onChangePhone(v: String) { state.value = state.value.copy(phone = v) }

    fun onPickImage(file: java.io.File) = viewModelScope.launch {
        try {
            state.value = state.value.copy(isLoading = true, error = null)
            val url = use.uploadImage(file)
            state.value = state.value.copy(photoUrl = url)
        } catch (t: Throwable) {
            state.value = state.value.copy(error = "Görsel yüklenemedi")
        } finally { state.value = state.value.copy(isLoading = false) }
    }

    fun onSave() = viewModelScope.launch {
        try {
            state.value = state.value.copy(isLoading = true, error = null)
            val c = Contact(
                id = state.value.contactId ?: java.util.UUID.randomUUID().toString(),
                firstName = state.value.firstName,
                lastName = state.value.lastName,
                phone = state.value.phone,
                photoUrl = state.value.photoUrl,
                isInDeviceContacts = false
            )
            if (state.value.contactId == null) use.add(c) else use.update(c)
            state.value = state.value.copy(isLoading = false, saved = true)
        } catch (t: Throwable) {
            state.value = state.value.copy(isLoading = false, error = "Kaydetme başarısız")
        }
    }

    fun onDelete() = viewModelScope.launch {
        val id = state.value.contactId ?: return@launch
        try { use.delete(id); state.value = state.value.copy(saved = true) }
        catch (_: Throwable) { state.value = state.value.copy(error = "Silme başarısız") }
    }
}