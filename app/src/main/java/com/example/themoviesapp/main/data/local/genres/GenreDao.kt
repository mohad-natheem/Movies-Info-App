package com.example.themoviesapp.main.data.local.genres

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(
        genresList: List<GenreEntity>
    )

    @Query(
        """
        SELECT * FROM GENREENTITY
        WHERE type = :mediaType
        """
    )
    suspend fun getGenres(
        mediaType: String
    ):List<GenreEntity>
}