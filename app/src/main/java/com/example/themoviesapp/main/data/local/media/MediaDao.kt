package com.example.themoviesapp.main.data.local.media

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MediaDao {

    @Insert(entity = MediaEntity::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaList(
        mediaEntities: List<MediaEntity>
    )

    @Insert(entity = MediaEntity::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insetMediaItem(
        mediaEntity: MediaEntity
    )

    @Query(
        """
            SELECT * FROM MEDIAENTITY
            WHERE mediaType = :mediaType AND category = :category
        """
    )
    suspend fun getMediaListByTypeAndCategory(
        mediaType: String,
        category: String
    ):List<MediaEntity>

    @Query(
        """
            SELECT * FROM MEDIAENTITY
            WHERE category = :category
        """
    )
    suspend fun getTrendingMediaList(
        category: String
    ):List<MediaEntity>

    @Query(
        """
            SELECT * FROM MEDIAENTITY
            WHERE id = :id
        """
    )
    suspend fun getMediaById(
        id: Int
    ):MediaEntity

    @Update
    suspend fun updateMediaItem(
        mediaEntity: MediaEntity
    )

    @Query(
        """
            DELETE FROM MEDIAENTITY
            WHERE mediaType = :mediaType AND category = :category
        """
    )
    suspend fun deleteMediaByTypeAndCategory(
        mediaType: String,
        category: String
    )

    @Query(
        """
            DELETE FROM MEDIAENTITY
            WHERE category= :category
        """
    )
    suspend fun deleteTrendingMediaList(
        category: String
    )

}