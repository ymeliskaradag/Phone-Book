package com.meliskaradag.telefonrehberiuygulamasi.data.repository

import android.util.Log
import com.meliskaradag.telefonrehberiuygulamasi.data.api.ApiService
import com.meliskaradag.telefonrehberiuygulamasi.data.db.ContactDao
import com.meliskaradag.telefonrehberiuygulamasi.data.mapper.toCreateUpdateBody
import com.meliskaradag.telefonrehberiuygulamasi.data.mapper.toDomain
import com.meliskaradag.telefonrehberiuygulamasi.data.mapper.toDomainList
import com.meliskaradag.telefonrehberiuygulamasi.data.mapper.toEntity
import com.meliskaradag.telefonrehberiuygulamasi.domain.model.Contact
import com.meliskaradag.telefonrehberiuygulamasi.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.io.File
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

private const val TAG = "ContactRepositoryImpl"

class ContactRepositoryImpl(
    private val api: ApiService,
    private val dao: ContactDao
) : ContactRepository {

    override fun getContactsStream(): Flow<List<Contact>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun refreshContactsFromApi() {
        try {
            val resp = api.getAllUsers()
            val body = resp.requireBodyOrThrow("getAllUsers")
            val domain = body.data?.toDomainList().orEmpty()
            dao.upsertAll(domain.map { it.toEntity() })
            Log.d(TAG, "refreshContactsFromApi: fetched=${domain.size}")
        } catch (t: Throwable) {
            Log.e(TAG, "refreshContactsFromApi failed", t)
            throw t
        }
    }

    override suspend fun add(contact: Contact) {
        try {
            val req = contact.toCreateUpdateBody()
            val resp = api.createUser(req)
            val created = resp.requireBodyOrThrow("createUser").data!!.toDomain()
            dao.upsert(created.toEntity())
            Log.d(TAG, "add: id=${created.id}")
        } catch (t: Throwable) {
            Log.e(TAG, "add failed", t)
            throw t
        }
    }

    override suspend fun update(contact: Contact) {
        try {
            val req = contact.toCreateUpdateBody()
            val resp = api.updateUser(contact.id, req)
            val updated = resp.requireBodyOrThrow("updateUser").data!!.toDomain()
            dao.upsert(updated.toEntity())
            Log.d(TAG, "update: id=${updated.id}")
        } catch (t: Throwable) {
            Log.e(TAG, "update failed", t)
            throw t
        }
    }

    override suspend fun delete(id: String) {
        try {
            val resp = api.deleteUser(id)
            val body = resp.requireBodyOrThrow("deleteUser")
            if (body.success) {
                dao.deleteById(id)
                Log.d(TAG, "delete: id=$id")
            } else {
                Log.e(TAG, "delete failed: success=false")
                throw HttpException(resp)
            }
        } catch (t: Throwable) {
            Log.e(TAG, "delete failed (exception)", t)
            throw t
        }
    }

    override suspend fun search(query: String): List<Contact> =
        try {
            dao.search(query).map { it.toDomain() }.also {
                Log.d(TAG, "search: q='$query' result=${it.size}")
            }
        } catch (t: Throwable) {
            Log.e(TAG, "search failed: q='$query'", t)
            throw t
        }

    override suspend fun uploadImage(file: File): String? {
        val request = file.asRequestBody("image/*".toMediaType())
        val part = MultipartBody.Part.createFormData("image", file.name, request)
        val resp = api.uploadImage(part)
        val body = resp.requireBodyOrThrow("uploadImage")
        return body.data?.imageUrl
    }
}

private fun <T> Response<T>.requireBodyOrThrow(op: String): T {
    if (!isSuccessful) {
        Log.e(TAG, "$op failed: code=${code()} msg=${message()}")
        throw HttpException(this)
    }
    val b = body()
    if (b == null) {
        Log.e(TAG, "$op failed: empty body")
        throw IOException("$op returned empty body")
    }
    return b
}