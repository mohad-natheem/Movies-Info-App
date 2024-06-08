package com.example.themoviesapp.main.presentation

import com.example.themoviesapp.main.domain.model.Genre
import com.example.themoviesapp.main.domain.model.Media

data class MainUiState(

    val popularMoviesPage: Int = 1,
    val topRatedMoviesPage: Int = 1,
    val nowPlayingMoviesPage: Int = 1,

    val popularTvSeriesPage: Int = 1,
    val topRatedTvSeriesPage: Int = 1,

    val trendingAllPage: Int = 1,

    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val areListsToBuildSpecialListEmpty: Boolean = true,

    val popularMoviesList: List<Media> = emptyList(),
    val topRatedMoviesList: List<Media> = emptyList(),
    val nowPlayingMoviesList: List<Media> = emptyList(),

    val popularTvSeriesList: List<Media> = emptyList(),
    val topRatedTvSeriesList: List<Media> = emptyList(),

    val trendingAllList: List<Media> = emptyList(),

    //popularTvSeriesList and topRatedTvSeriesList
    val tvSeriesList: List<Media> = emptyList(),

    //topRatedMoviesList and topRatedTvSeriesList
    val topRatedAllList: List<Media> = emptyList(),

    //nowPlayingMoviesList and nowPlayingTvSeriesList
    val recommendedAllList: List<Media> = emptyList(),

    //matching items in
    //recommendedAllList and tredingAllList
    val specialList: List<Media> = emptyList(),

    val moviesGenreList: List<Genre> = emptyList(),
    val tvGenreList: List<Genre> = emptyList(),

    )
