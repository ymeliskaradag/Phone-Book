package com.meliskaradag.telefonrehberiuygulamasi.di

import android.content.Context
import androidx.room.Room
import com.meliskaradag.telefonrehberiuygulamasi.data.api.ApiClient
import com.meliskaradag.telefonrehberiuygulamasi.data.api.ApiService
import com.meliskaradag.telefonrehberiuygulamasi.data.db.AppDatabase
import com.meliskaradag.telefonrehberiuygulamasi.data.repository.ContactRepositoryImpl
import com.meliskaradag.telefonrehberiuygulamasi.domain.repository.ContactRepository
import com.meliskaradag.telefonrehberiuygulamasi.domain.usecase.*

object ServiceLocator {

    private const val BASE_URL = "http://146.59.52.68:11235/"
    private const val API_KEY  = "d6223244-5a7a-4d84-b000-24d9a76e0045"
    //val api = ApiClient.create(BuildConfig.BASE_URL, BuildConfig.API_KEY)

    // singleton nesneler
    @Volatile private var api: ApiService? = null
    @Volatile private var db: AppDatabase? = null
    @Volatile private var repo: ContactRepository? = null

    private fun provideApi(): ApiService =
        api ?: synchronized(this) {
            api ?: ApiClient.create(BASE_URL, API_KEY).also { api = it }
        }

    private fun provideDb(context: Context): AppDatabase =
        db ?: synchronized(this) {
            db ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "contacts.db"
            )
                .fallbackToDestructiveMigration(true)
                .build()
                .also { db = it }
        }

    private fun provideRepository(context: Context): ContactRepository =
        repo ?: synchronized(this) {
            val dao = provideDb(context).contactDao()
            ContactRepositoryImpl(
                api = provideApi(),
                dao = dao
            ).also { repo = it }
        }


    fun provideUseCases(context: Context) = UseCases(
        get = GetContactsUseCase(provideRepository(context)),
        refresh = RefreshContactsUseCase(provideRepository(context)),
        add = AddContactUseCase(provideRepository(context)),
        update = UpdateContactUseCase(provideRepository(context)),
        delete = DeleteContactUseCase(provideRepository(context)),
        search = SearchContactsUseCase(provideRepository(context)),
        uploadImage = UploadImageUseCase(provideRepository(context)) // (Swagger'daki UploadImage)
    )
}