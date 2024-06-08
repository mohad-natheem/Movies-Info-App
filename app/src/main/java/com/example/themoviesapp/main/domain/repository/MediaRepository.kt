package com.example.themoviesapp.main.domain.repository

import com.example.themoviesapp.main.domain.model.Media
import com.example.themoviesapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun updateItem(
        media: Media
    )

    suspend fun insertItem(
        media: Media
    )

    suspend fun getItem(
        type: String,
        category: String,
        id: Int
    ):Media

    suspend fun getMoviesAndTvSeriesList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        category: String,
        page: Int,
        apiKey: String,
    ):Flow<Resource<List<Media>>>

    suspend fun getTrendingList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        time: String,
        page: Int,
        apiKey: String
    ):Flow<Resource<List<Media>>>
}