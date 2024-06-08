package com.example.themoviesapp.main.presentation.media_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.example.themoviesapp.main.domain.model.Media
import com.example.themoviesapp.main.presentation.MainUiState
import com.example.themoviesapp.main.presentation.SCREENS
import com.example.themoviesapp.theme.font
import com.example.themoviesapp.util.Item
import com.example.themoviesapp.util.MediaItem

@Composable
fun MediaListScreen(
    mainUiState: MainUiState,
    navBackStackEntry: NavBackStackEntry,

) {
    val mediaType = navBackStackEntry.arguments?.getString("type")
    val type = when(mediaType){
        "trendingAllListScreen" -> SCREENS.TRENDING_ALL_LIST_SCREEN
        "topRatedAllListScreen" -> SCREENS.TOP_RATED_ALL_LIST_SCREEN
        "recommendedListScreen" -> SCREENS.RECOMMENDED_ALL_LIST_SCREEN
        "popularScreen" -> SCREENS.POPULAR_SCREEN
         "tvSeriesScreen" -> SCREENS.TV_SERIES_SCREEN
        else -> SCREENS.SEARCH_SCREEN

    }
    val title = when (type) {
        SCREENS.TRENDING_ALL_LIST_SCREEN -> {
            "Trending Now"
        }

        SCREENS.TV_SERIES_SCREEN -> {
            "Tv Series"
        }

        SCREENS.RECOMMENDED_ALL_LIST_SCREEN -> {
            "Recommended"
        }

        SCREENS.POPULAR_SCREEN -> {
            "Popular"
        }
        SCREENS.TOP_RATED_ALL_LIST_SCREEN ->{
            "Top Rated"
        }
        else -> {
            ""
        }
    }
    val mediaList: List<Media> = when (type) {
        SCREENS.TRENDING_ALL_LIST_SCREEN -> {
            mainUiState.trendingAllList
        }

        SCREENS.TV_SERIES_SCREEN -> {
            mainUiState.tvSeriesList
        }

        SCREENS.RECOMMENDED_ALL_LIST_SCREEN -> {
            mainUiState.recommendedAllList
        }
        SCREENS.POPULAR_SCREEN ->{
            mainUiState.recommendedAllList
        }
        SCREENS.TOP_RATED_ALL_LIST_SCREEN -> {
            mainUiState.topRatedAllList
        }
        else ->{
            emptyList<Media>()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ){
        Column{
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = font
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(mediaList.size) { mediaIndex ->
                    MediaItem(media = mediaList[mediaIndex], mainUiState = mainUiState)
                }

            }
        }
    }

}