package com.meliskaradag.telefonrehberiuygulamasi.data.mapper

import com.meliskaradag.telefonrehberiuygulamasi.data.api.GetAllResponse
import com.meliskaradag.telefonrehberiuygulamasi.data.api.UserCreateUpdateBody
import com.meliskaradag.telefonrehberiuygulamasi.data.api.UserDto
import com.meliskaradag.telefonrehberiuygulamasi.data.db.ContactEntity
import com.meliskaradag.telefonrehberiuygulamasi.domain.model.Contact
import java.util.UUID

// API -> Domain
fun UserDto.toDomain(): Contact = Contact(
    id = id.ifBlank { UUID.randomUUID().toString() }, //güvenlik amaçlı
    firstName = firstName,
    lastName = lastName,
    phone = phoneNumber,
    photoUrl = profileImageUrl,
    isInDeviceContacts = false
)

//Domain -> API (Create/Update body)
fun Contact.toCreateUpdateBody(): UserCreateUpdateBody = UserCreateUpdateBody(
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phone,
    profileImageUrl = photoUrl
)

//GetAll cevabındaki listeyi domain listesine çevirir
fun GetAllResponse.toDomainList(): List<Contact> = users.map { it.toDomain() }

//Room Entity -> Domain
fun ContactEntity.toDomain(): Contact = Contact(
    id = id,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    photoUrl = photoUrl,
    isInDeviceContacts = isInDeviceContacts
)

fun Contact.toEntity(): ContactEntity = ContactEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    photoUrl = photoUrl,
    isInDeviceContacts = isInDeviceContacts
)