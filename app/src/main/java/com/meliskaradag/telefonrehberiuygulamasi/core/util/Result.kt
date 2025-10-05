package com.meliskaradag.telefonrehberiuygulamasi.core.util

sealed interface Result<out T> {
    data class Success<T>(val data: T): Result<T>
    data class Error(val message: String, val cause: Throwable? = null): Result<Nothing>
    object Loading: Result<Nothing>
}