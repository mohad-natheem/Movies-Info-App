package com.example.themoviesapp.media_details.presentation

import com.example.themoviesapp.main.domain.repository.MediaRepository
import com.example.themoviesapp.media_details.domain.repository.DetailsRepository
import com.example.themoviesapp.media_details.domain.repository.ExtraDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    val mediaRepository: MediaRepository,
    val extraDetailsRepository: ExtraDetailsRepository,
    val detailsRepository: DetailsRepository
) {

    private val _mediaDetailsScreenState = MutableStateFlow(MediaDetailsScreenState())
    val mediaDetailsScreenState = _mediaDetailsScreenState.asStateFlow()

    fun onEvent(event: MediaDetailsScreenEvents) {
        when (event) {
            is MediaDetailsScreenEvents.NavigateToWatchVideo -> {
                _mediaDetailsScreenState.update {
                    it.copy(
                        videoId = mediaDetailsScreenState.value.videoList.shuffled()[0]
                    )
                }
            }

            is MediaDetailsScreenEvents.Refresh -> {
                _mediaDetailsScreenState.update {
                    it.copy(isLoading = true)
                }

                startLoad(isRefresh = true)

            }

            is MediaDetailsScreenEvents.SetDataAndLoad -> {
                _mediaDetailsScreenState.update {
                    it.copy(
                        movieGenreList = event.moviesGenreslist,
                        tvGenreList = event.tvSeriesGenreList
                    )
                }

                startLoad(
                    isRefresh = false,
                    type = event.type,
                    category = event.category,
                    id = event.id
                )
            }
        }
    }

    private fun startLoad(
        isRefresh: Boolean,
        type: String = mediaDetailsScreenState.value.media?.mediaType?:"",
        category: String = mediaDetailsScreenState.value.media?.category?:"",
        id: Int = mediaDetailsScreenState.value.media?.id?: 0
    ) {
        loadMediaItem(
            id = id,
            type = type,
            category = category
        ){
        }

    }

    private fun loadMediaItem(
        id: Int,
        type: String,
        category: String,
        onFinished: () -> Unit
    ) {
        viewModelScope.launch{
            _mediaDetailsScreenState.update {
                it.copy(
                    media = mediaRepository.getItem(
                        type = type,
                        category = category,
                        id = id
                    )
                )
            }
        }

    }
}