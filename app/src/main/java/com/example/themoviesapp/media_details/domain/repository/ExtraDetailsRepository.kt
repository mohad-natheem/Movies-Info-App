package com.example.themoviesapp.media_details.domain.repository

import com.example.themoviesapp.main.domain.model.Media
import com.example.themoviesapp.media_details.data.remote.dto.video.VideosList
import com.example.themoviesapp.media_details.domain.models.Cast
import com.example.themoviesapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ExtraDetailsRepository {

    suspend fun getSimilarMediaList(
        isRefresh: Boolean,
        type: String,
        id: Int,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Media>>>

    suspend fun getCastList(
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Cast>>

    suspend fun getVideosList(
        isRefresh: Boolean,
        type: String,
        apiKey: String,
        id: Int
    ): Flow<Resource<List<String>>>
}