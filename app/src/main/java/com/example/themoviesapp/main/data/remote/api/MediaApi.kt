package com.example.themoviesapp.main.data.remote.api

import com.example.themoviesapp.main.data.remote.dto.MediaListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MediaApi {

    @GET("{type}/{category}")
    suspend fun getMoviesAndTvSeriesList(
        @Path("type") type:String,
        @Path("category") category:String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ):MediaListDto

    @GET("trending/{type}/{time}")
    suspend fun getTrendingList(
        @Path("type") type:String,
        @Path("time") time:String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ):MediaListDto

    @GET("search/multi")
    suspend fun getSearchList(
        @Query("query")query: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ):MediaListDto

    companion object{
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = "2013e855c80bc9b8a84e354cc64ac631"
    }
}