package com.example.themoviesapp.media_details.presentation

import com.example.themoviesapp.main.domain.model.Genre
import com.example.themoviesapp.main.domain.model.Media

data class MediaDetailsScreenState(
    val isLoading: Boolean = false,

    val media: Media? = null,

    val videoId: String = "",

    val readableTime: String = "",

    val similarMediaList: List<Media> = emptyList(),
    val smallSimilarMediaList: List<Media> = emptyList(),

    val videoList: List<String> = emptyList(),
    val movieGenreList: List<Genre> = emptyList(),
    val tvGenreList: List<Genre> = emptyList()
)
