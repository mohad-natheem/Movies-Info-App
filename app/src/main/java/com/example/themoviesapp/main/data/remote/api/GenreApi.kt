package com.example.themoviesapp.main.data.remote.api

import com.example.themoviesapp.main.data.remote.dto.GenresListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GenreApi {

    @GET("genre/{type}/list")
    suspend fun getGenresList(
        @Path("type") type: String,
        @Query("api_key") apiKey: String
    ):GenresListDto
}