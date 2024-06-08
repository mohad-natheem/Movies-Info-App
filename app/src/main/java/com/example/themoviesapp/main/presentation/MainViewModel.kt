package com.example.themoviesapp.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.recursiveFetchHashMap
import com.example.themoviesapp.main.data.remote.api.MediaApi.Companion.API_KEY
import com.example.themoviesapp.main.domain.model.Media
import com.example.themoviesapp.main.domain.repository.GenreRepository
import com.example.themoviesapp.main.domain.repository.MediaRepository
import com.example.themoviesapp.util.Constants
import com.example.themoviesapp.util.Constants.ALL
import com.example.themoviesapp.util.Constants.MOVIE
import com.example.themoviesapp.util.Constants.NOW_PLAYING
import com.example.themoviesapp.util.Constants.POPULAR
import com.example.themoviesapp.util.Constants.TOP_RATED
import com.example.themoviesapp.util.Constants.TV
import com.example.themoviesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {
    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    init {
        load()

    }

    private fun load(fetchFromRemote: Boolean = false) {
        loadPopularMovies(fetchFromRemote)
        loadTopRatedMovies(fetchFromRemote)
        loadNowPlayingMovies(fetchFromRemote)

        loadPopularTvSeries(fetchFromRemote)
        loadTopRatedTvSeries(fetchFromRemote)

        loadTrendingAll(fetchFromRemote)

        loadGenres(
            fetchFromRemote = fetchFromRemote,
            isMovies = false
        )

        loadGenres(
            fetchFromRemote = fetchFromRemote,
            isMovies = true
        )

    }

    fun onEvent(event: MainUiEvents) {
        when (event) {
            is MainUiEvents.Refresh -> {

                _mainUiState.update {
                    it.copy(
                        isLoading = true
                    )
                }

                loadGenres(
                    fetchFromRemote = true,
                    isMovies = false
                )

                loadGenres(
                    fetchFromRemote = true,
                    isMovies = true
                )

                when (event.type) {
                    SCREENS.TRENDING_ALL_LIST_SCREEN -> {
                        loadTrendingAll(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }
                    SCREENS.TOP_RATED_ALL_LIST_SCREEN -> {
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }
                    SCREENS.RECOMMENDED_ALL_LIST_SCREEN -> {
                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }
                    SCREENS.POPULAR_SCREEN -> {
                        loadPopularMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }
                    SCREENS.TV_SERIES_SCREEN -> {
                        loadPopularTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )

                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }
                    SCREENS.HOME_SCREEN -> {
                        loadTrendingAll(
                            fetchFromRemote = true,
                            isRefresh = true
                        )

                        loadTopRatedMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )

                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                            isRefresh = true
                        )

                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                            isRefresh = true
                        )
                    }

                    SCREENS.SEARCH_SCREEN -> {}
                }
            }

            is MainUiEvents.OnPaginate -> {
                when(event.type){
                    SCREENS.TRENDING_ALL_LIST_SCREEN -> {
                        loadTrendingAll(
                            fetchFromRemote = true,
                        )
                    }
                    SCREENS.TOP_RATED_ALL_LIST_SCREEN -> {
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                        )
                        loadTopRatedMovies(
                            fetchFromRemote = true,
                        )
                    }
                    SCREENS.RECOMMENDED_ALL_LIST_SCREEN -> {
                        loadNowPlayingMovies(
                            fetchFromRemote = true,
                        )
                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                        )
                    }
                    SCREENS.POPULAR_SCREEN -> {
                        loadPopularMovies(
                            fetchFromRemote = true,
                        )
                    }
                    SCREENS.TV_SERIES_SCREEN -> {
                        loadPopularTvSeries(
                            fetchFromRemote = true,
                        )

                        loadTopRatedTvSeries(
                            fetchFromRemote = true,
                        )
                    }

                    SCREENS.SEARCH_SCREEN -> {}
                    SCREENS.HOME_SCREEN -> {}
                }

            }
        }
    }

    private fun loadGenres(
        fetchFromRemote: Boolean,
        isMovies: Boolean
    ) {
        viewModelScope.launch {
            if (isMovies) {
                genreRepository
                    .getGenres(fetchFromRemote, MOVIE, API_KEY)
                    .collect { result ->
                        when (result) {
                            is Resource.Error -> {}
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                result.data?.let {genreList ->
                                    _mainUiState.update {
                                        it.copy(
                                            moviesGenreList = genreList
                                        )

                                    }
                                }
                            }
                        }

                    }
            }
            else{
                genreRepository
                    .getGenres(fetchFromRemote, TV, API_KEY)
                    .collect { result ->
                        when (result) {
                            is Resource.Error -> {}
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                result.data?.let {genreList ->
                                    _mainUiState.update {
                                        it.copy(
                                            tvGenreList = genreList
                                        )

                                    }
                                }
                            }
                        }

                    }

            }
        }

    }

    private fun loadPopularMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ){
        viewModelScope.launch {

            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote = fetchFromRemote,
                    isRefresh = isRefresh,
                    type = MOVIE,
                    category = POPULAR,
                    page = mainUiState.value.popularMoviesPage,
                    apiKey = API_KEY
                )
                .collect{result ->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let {mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if(isRefresh){
                                    _mainUiState.update {
                                        it.copy(
                                            popularMoviesList = shuffledMediaList,
                                            popularMoviesPage = 1
                                        )
                                    }
                                }
                                else{
                                    _mainUiState.update {
                                        it.copy(
                                            popularMoviesList = mainUiState.value.popularMoviesList + shuffledMediaList,
                                            popularMoviesPage = mainUiState.value.popularMoviesPage + 1
                                        )
                                    }
                                }

                            }
                        }
                        is Resource.Error -> {}
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }

                }
        }

    }

    private fun loadTopRatedMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ){
        viewModelScope.launch {
            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote = fetchFromRemote,
                    isRefresh = isRefresh,
                    type = MOVIE,
                    category = TOP_RATED,
                    page = mainUiState.value.topRatedMoviesPage,
                    apiKey = API_KEY
                )
                .collect{result ->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let {mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if(isRefresh){
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedMoviesList = shuffledMediaList,
                                            topRatedMoviesPage = 1
                                        )
                                    }
                                }
                                else{
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedMoviesList = mainUiState.value.topRatedMoviesList + shuffledMediaList,
                                            topRatedMoviesPage = mainUiState.value.topRatedMoviesPage + 1
                                        )
                                    }
                                }
                                createTopRatedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )

                            }
                        }
                        is Resource.Error -> {}
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }

                }
        }

    }
    private fun loadNowPlayingMovies(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ){
        viewModelScope.launch {
            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote = fetchFromRemote,
                    isRefresh = isRefresh,
                    type = MOVIE,
                    category = NOW_PLAYING,
                    page = mainUiState.value.nowPlayingMoviesPage,
                    apiKey = API_KEY
                )
                .collect{result ->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let {mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if(isRefresh){
                                    _mainUiState.update {
                                        it.copy(
                                            nowPlayingMoviesList = shuffledMediaList,
                                            nowPlayingMoviesPage = 1
                                        )
                                    }
                                }
                                else{
                                    _mainUiState.update {
                                        it.copy(
                                            nowPlayingMoviesList = mainUiState.value.nowPlayingMoviesList + shuffledMediaList,
                                            nowPlayingMoviesPage = mainUiState.value.nowPlayingMoviesPage + 1
                                        )
                                    }
                                }
                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )


                            }
                        }
                        is Resource.Error -> {}
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadTopRatedTvSeries(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ){
        viewModelScope.launch {
            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote = fetchFromRemote,
                    isRefresh = isRefresh,
                    type = TV,
                    category = TOP_RATED,
                    page = mainUiState.value.topRatedTvSeriesPage,
                    apiKey = API_KEY
                )
                .collect{result ->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let {mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if(isRefresh){
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedTvSeriesList = shuffledMediaList,
                                            topRatedTvSeriesPage = 1
                                        )
                                    }
                                }
                                else{
                                    _mainUiState.update {
                                        it.copy(
                                            topRatedTvSeriesList = mainUiState.value.topRatedTvSeriesList + shuffledMediaList,
                                            topRatedTvSeriesPage = mainUiState.value.topRatedTvSeriesPage + 1
                                        )
                                    }
                                }
                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                                createTvSeriesList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                                createTopRatedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )

                            }
                        }
                        is Resource.Error -> {}
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadPopularTvSeries(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ){
        viewModelScope.launch {
            mediaRepository
                .getMoviesAndTvSeriesList(
                    fetchFromRemote = fetchFromRemote,
                    isRefresh = isRefresh,
                    type = TV,
                    category = POPULAR,
                    page = mainUiState.value.popularTvSeriesPage,
                    apiKey = API_KEY
                )
                .collect{result ->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let {mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if(isRefresh){
                                    _mainUiState.update {
                                        it.copy(
                                            popularTvSeriesList = shuffledMediaList,
                                            popularTvSeriesPage = 1
                                        )
                                    }
                                }
                                else{
                                    _mainUiState.update {
                                        it.copy(
                                            popularTvSeriesList = mainUiState.value.popularTvSeriesList + shuffledMediaList,
                                            popularTvSeriesPage = mainUiState.value.popularTvSeriesPage + 1
                                        )
                                    }
                                }
                                createTvSeriesList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }
                        is Resource.Error -> {}
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadTrendingAll(
        fetchFromRemote: Boolean = false,
        isRefresh: Boolean = false
    ){
        viewModelScope.launch {
            mediaRepository
                .getTrendingList(
                    fetchFromRemote = fetchFromRemote,
                    isRefresh = isRefresh,
                    type = ALL,
                    time = Constants.TRENDING_TIME,
                    page = mainUiState.value.topRatedTvSeriesPage,
                    apiKey = API_KEY
                )
                .collect{result ->
                    when(result){
                        is Resource.Success ->{
                            result.data?.let {mediaList ->
                                val shuffledMediaList = mediaList.toMutableList()
                                shuffledMediaList.shuffle()

                                if(isRefresh){
                                    _mainUiState.update {
                                        it.copy(
                                            trendingAllList = shuffledMediaList,
                                            trendingAllPage = 1
                                        )
                                    }
                                }
                                else{
                                    _mainUiState.update {
                                        it.copy(
                                            trendingAllList = mainUiState.value.trendingAllList + shuffledMediaList,
                                            trendingAllPage = mainUiState.value.trendingAllPage + 1
                                        )
                                    }
                                }
                                createRecommendedMediaAllList(
                                    mediaList = mediaList,
                                    isRefresh = isRefresh
                                )
                            }
                        }
                        is Resource.Error -> {}
                        is Resource.Loading -> {
                            _mainUiState.update {
                                it.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun createSpecialList(
        mediaList: List<Media>,
        isRefresh: Boolean = false
    ){
        if(isRefresh){
            _mainUiState.update {
                it.copy(
                    specialList = emptyList()
                )
            }
        }

        if(mainUiState.value.specialList.size >= 7){
            return
        }
        val shuffledList = mediaList.take(7).toMutableList()
        shuffledList.shuffle()

        if(isRefresh){
            _mainUiState.update {
                it.copy(
                    specialList = shuffledList.toList()
                )
            }
        }
        else{
            _mainUiState.update {
                it.copy(
                    specialList = mainUiState.value.specialList + shuffledList.toList()
                )
            }
        }
        for(item in mainUiState.value.specialList){
            Timber.tag("special_list").d(item.title)
        }
    }

    private fun createTvSeriesList(
        mediaList: List<Media>,
        isRefresh: Boolean = false
    ){
        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if(isRefresh){
            _mainUiState.update {
                it.copy(
                    tvSeriesList = shuffledMediaList.toList()
                )
            }
        }else{
            _mainUiState.update {
                it.copy(
                    tvSeriesList = mainUiState.value.tvSeriesList + shuffledMediaList.toList()
                )
            }
        }

    }

    private fun createTopRatedMediaAllList(
        mediaList: List<Media>,
        isRefresh: Boolean = false
    ){
        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if(isRefresh){
            _mainUiState.update {
                it.copy(
                    topRatedAllList = shuffledMediaList.toList()
                )
            }
        }else{
            _mainUiState.update {
                it.copy(
                    topRatedAllList = mainUiState.value.topRatedAllList + shuffledMediaList.toList()
                )
            }
        }

    }

    private fun createRecommendedMediaAllList(
        mediaList: List<Media>,
        isRefresh: Boolean = false
    ){
        val shuffledMediaList = mediaList.toMutableList()
        shuffledMediaList.shuffle()

        if(isRefresh){
            _mainUiState.update {
                it.copy(
                    recommendedAllList = shuffledMediaList.toList(),
                )
            }
        }
        else{
            _mainUiState.update {
                it.copy(
                    recommendedAllList = mainUiState.value.recommendedAllList + shuffledMediaList.toList()
                )
            }
        }
        createSpecialList(
            mediaList = mediaList,
            isRefresh = isRefresh
        )

    }

}


enum class SCREENS {
    TRENDING_ALL_LIST_SCREEN,
    TOP_RATED_ALL_LIST_SCREEN,
    RECOMMENDED_ALL_LIST_SCREEN,
    POPULAR_SCREEN,
    TV_SERIES_SCREEN,
    SEARCH_SCREEN,
    HOME_SCREEN
}