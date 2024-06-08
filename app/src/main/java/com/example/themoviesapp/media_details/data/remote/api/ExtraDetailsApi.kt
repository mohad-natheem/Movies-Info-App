package com.example.themoviesapp.media_details.data.remote.api

import com.example.themoviesapp.main.data.remote.dto.MediaListDto
import com.example.themoviesapp.media_details.data.remote.dto.details.DetailsDto
import com.example.themoviesapp.media_details.data.remote.dto.video.VideosList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExtraDetailsApi {

    @GET("{type}/{id}")
    suspend fun getDetails(
        @Path("type") type: String,
        @Path("id") id:Int,
        @Query("api_key") apiKey: String
    ): DetailsDto

    @GET("{type}/{id}/similar")
    suspend fun getSimilarMedia(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): MediaListDto

    @GET("{type}/{id}/videos")
    suspend fun getVideos(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): VideosList


}