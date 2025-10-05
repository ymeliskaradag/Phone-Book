package com.meliskaradag.telefonrehberiuygulamasi.core.util

object Validators { //telefon ve boşluk kontrolü için
    fun isValidPhone(phone: String) = phone.trim().length in 7..15
    fun isNotBlank(value: String) = value.trim().isNotEmpty()
}