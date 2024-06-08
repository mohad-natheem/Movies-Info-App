package com.example.themoviesapp.main.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.themoviesapp.main.presentation.MainUiState
import com.example.themoviesapp.main.presentation.SCREENS
import com.example.themoviesapp.theme.font
import com.example.themoviesapp.util.BottomNavRoute
import com.example.themoviesapp.util.Constants
import com.example.themoviesapp.util.Item
import com.example.themoviesapp.util.Radius
import com.example.themoviesapp.util.shimmerEffect


@Composable
fun ShouldShowShimmerorHomeSection(
    bottomNavController: NavController,
    type: SCREENS,
    mainUiState: MainUiState,
    showShimmer: Boolean
) {
    val navType = when(type){
        SCREENS.TRENDING_ALL_LIST_SCREEN ->"trendingAllListScreen"
        SCREENS.TOP_RATED_ALL_LIST_SCREEN -> "topRatedAllListScreen"
        SCREENS.RECOMMENDED_ALL_LIST_SCREEN->"recommendedListScreen"
        SCREENS.POPULAR_SCREEN->"popularScreen"
        SCREENS.TV_SERIES_SCREEN->"tvSeriesScreen"
        else -> "searchScreen"

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

        else -> {
            "Top Rated"
        }
    }
    Column(
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = font
            )
            if (!showShimmer) {
                Text(
                    modifier = Modifier.clickable {
                        bottomNavController.navigate("${BottomNavRoute.MEDIA_LIST_SCREEN}?type=${navType}"){
                            launchSingleTop = true
                        }
                    },
                    text = "see all",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = font,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            } else {
                Box(modifier = Modifier.weight(1f))
            }
        }
        if (showShimmer) {
            ShowHomeShimmer(
                modifier = Modifier
                    .height(200.dp)
                    .width(150.dp)
            )
        } else {
            HomeScreenSection(
                type = type,
                mainUiState = mainUiState
            )
        }
    }
}

@Composable
fun HomeScreenSection(
    modifier: Modifier = Modifier,
    type: SCREENS,
    mainUiState: MainUiState
) {
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
        else -> {
            "Top Rated"
        }
    }
    val mediaList = when (type) {
        SCREENS.TRENDING_ALL_LIST_SCREEN -> {
            mainUiState.trendingAllList.take(10)

        }
        SCREENS.TV_SERIES_SCREEN -> {
            mainUiState.tvSeriesList.take(10)
        }
        SCREENS.RECOMMENDED_ALL_LIST_SCREEN -> {
            mainUiState.recommendedAllList.take(10)
        }
        else -> {
            mainUiState.topRatedAllList.take(10)
        }
    }
    LazyRow(
        Modifier.fillMaxWidth()
    ) {
        item {
            Spacer(modifier = Modifier.width(16.dp))
        }
        items(mediaList.size) {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(Radius.dp))
            ) {
                Item(
                    media = mediaList[it],
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = if(it==9)16.dp else 0.dp
                        )
                        .height(200.dp)
                        .width(150.dp)
                )
            }
        }
    }
}

@Composable
fun ShowHomeShimmer(
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Spacer(modifier = Modifier.width(16.dp))
        }
        items(10) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .width(150.dp)
                    .padding(
                        start = 16.dp,
                        end = if(it==9)16.dp else 0.dp
                    )
                    .clip(RoundedCornerShape(Radius.dp))
                    .shimmerEffect(false)

            )
        }

    }


}
