package com.example.themoviesapp.media_details.presentation

import com.example.themoviesapp.main.domain.model.Genre

sealed class MediaDetailsScreenEvents {

    data class SetDataAndLoad(
        val moviesGenreslist: List<Genre>,
        val tvSeriesGenreList: List<Genre>,
        val id: Int,
        val type: String,
        val category: String
    ): MediaDetailsScreenEvents()

    object Refresh: MediaDetailsScreenEvents()

    object NavigateToWatchVideo: MediaDetailsScreenEvents()
}

