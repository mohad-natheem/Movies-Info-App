package com.example.themoviesapp.media_details.domain.repository

import com.example.themoviesapp.main.domain.model.Media
import com.example.themoviesapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {

    suspend fun getDetails(
        type: String,
        id: Int,
        apikey: String,
        isRefresh: Boolean
    ): Flow<Resource<Media>>
}