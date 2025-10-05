package com.meliskaradag.telefonrehberiuygulamasi.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts ORDER BY firstName, lastName")
    fun observeAll(): Flow<List<ContactEntity>>

    @Query(""" SELECT * FROM contacts WHERE (firstName || ' ' || lastName) LIKE '%' || :q || '%' OR phone LIKE '%' || :q || '%'
        ORDER BY firstName, lastName """)
    suspend fun search(q: String): List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ContactEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(list: List<ContactEntity>)

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteById(id: String)
}