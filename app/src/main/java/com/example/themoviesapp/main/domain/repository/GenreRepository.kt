package com.example.themoviesapp.main.domain.repository

import com.example.themoviesapp.main.domain.model.Genre
import com.example.themoviesapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface GenreRepository {

    suspend fun getGenres(
        fetchFromRemote: Boolean,
        type: String,
        apiKey: String
    ):Flow<Resource<List<Genre>>>
}