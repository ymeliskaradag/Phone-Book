package com.meliskaradag.telefonrehberiuygulamasi.presentation.contacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.meliskaradag.telefonrehberiuygulamasi.di.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ContactsViewModel(app: Application) : AndroidViewModel(app) {
    private val use = ServiceLocator.provideUseCases(app)
    private val _state = MutableStateFlow(ContactsUiState(isLoading = true))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            use.get().collectLatest { list ->
                _state.value = _state.value.copy(
                    contacts = list,
                    grouped = list.groupBy { it.firstLetter },
                    isLoading = false, error = null
                )
            }
        }
        onEvent(ContactsEvent.OnRefresh)
    }

    fun onEvent(e: ContactsEvent) {
        when (e) {
            is ContactsEvent.OnQueryChange ->
                _state.value = _state.value.copy(query = e.value)

            ContactsEvent.OnSearch -> viewModelScope.launch {
                try {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                    val result = use.search(_state.value.query)
                    _state.value = _state.value.copy(
                        contacts = result,
                        grouped = result.groupBy { it.firstLetter },
                        isLoading = false,
                        recentQueries = (listOf(_state.value.query) + _state.value.recentQueries)
                            .distinct().take(5)
                    )
                } catch (t: Throwable) {
                    _state.value = _state.value.copy(isLoading = false, error = "Arama başarısız")
                }
            }

            is ContactsEvent.OnDelete -> viewModelScope.launch {
                try { use.delete(e.id) } catch (_: Throwable) {
                    _state.value = _state.value.copy(error = "Silme başarısız")
                }
            }

            ContactsEvent.OnRefresh -> viewModelScope.launch {
                try { use.refresh() } catch (t: Throwable) {
                    _state.value = _state.value.copy(error = "Yenileme başarısız")
                }
            }
        }
    }
}