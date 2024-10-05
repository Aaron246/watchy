package com.aaron.watchy.main.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MediaDao {

    @Upsert
    suspend fun upsertMediaList(mediaEntities: List<MediaEntity>)

    @Upsert
    suspend fun upsertMediaItem(mediaEntity: MediaEntity)

    @Query("SELECT * FROM MediaEntity")
    suspend fun getAllMediaList(): List<MediaEntity>

    @Query("SELECT * FROM MediaEntity WHERE mediaType = :mediaType AND category = :category")
    suspend fun getMediaListByTypeAndCategory(
        mediaType: String,
        category: String
    ): List<MediaEntity>

    @Query("SELECT * FROM MediaEntity WHERE category = :category")
    suspend fun getMediaListByCategory(
        category: String
    ): List<MediaEntity>

    @Query("SELECT * FROM MediaEntity WHERE mediaId = :mediaId")
    suspend fun getMediaById(
        mediaId: Int
    ): MediaEntity

    @Query("SELECT COUNT(*) FROM MediaEntity WHERE mediaId = :mediaId")
    suspend fun doesMediaItemExists(
        mediaId: Int
    ): Int

    @Query("DELETE FROM MediaEntity")
    suspend fun deleteAllMediaItem()

    @Query("DELETE FROM MediaEntity WHERE mediaType = :mediaType AND category = :category")
    suspend fun deleteMediaListByTypeAndCategory(
        mediaType: String,
        category: String
    )

    @Query("DELETE FROM MediaEntity WHERE category = :category")
    suspend fun deleteMediaListByCategory(
        category: String
    )
}