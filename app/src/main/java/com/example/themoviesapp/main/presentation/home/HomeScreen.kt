package com.example.themoviesapp.main.presentation.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.themoviesapp.main.presentation.MainUiState
import com.example.themoviesapp.main.presentation.SCREENS
import com.example.themoviesapp.theme.font
import com.example.themoviesapp.util.AutoSwipeSection
import com.example.themoviesapp.util.Radius
import com.example.themoviesapp.util.shimmerEffect

@Composable
fun HomeScreen(
    navHostController: NavController,
    bottomNavController: NavController,
    mainUiState: MainUiState
) {
    val context = LocalContext.current
    BackHandler(
        enabled = true
    ) {
        (context as Activity).finish()
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ShouldShowShimmerorHomeSection(
                bottomNavController = bottomNavController,
                type = SCREENS.TRENDING_ALL_LIST_SCREEN,
                mainUiState = mainUiState,
                showShimmer = mainUiState.trendingAllList.isEmpty()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = "Special",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = font
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (mainUiState.specialList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(Radius.dp))
                        .height(220.dp)
                        .shimmerEffect(false)
                        .align(Alignment.CenterHorizontally)
                )


            } else {
                AutoSwipeSection(
                    mediaList = mainUiState.specialList.take(7),
                    modifier = Modifier
                        .fillMaxWidth()

                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            ShouldShowShimmerorHomeSection(
                bottomNavController = bottomNavController,
                type = SCREENS.TV_SERIES_SCREEN,
                mainUiState = mainUiState,
                showShimmer = mainUiState.trendingAllList.isEmpty()
            )
            Spacer(modifier = Modifier.height(20.dp))
            ShouldShowShimmerorHomeSection(
                bottomNavController = bottomNavController,
                type = SCREENS.TOP_RATED_ALL_LIST_SCREEN,
                mainUiState = mainUiState,
                showShimmer = mainUiState.trendingAllList.isEmpty()
            )
            Spacer(modifier = Modifier.height(20.dp))
            ShouldShowShimmerorHomeSection(
                bottomNavController = bottomNavController,
                type = SCREENS.RECOMMENDED_ALL_LIST_SCREEN,
                mainUiState = mainUiState,
                showShimmer = mainUiState.trendingAllList.isEmpty()
            )
        }

    }

}