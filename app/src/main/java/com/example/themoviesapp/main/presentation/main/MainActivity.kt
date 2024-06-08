package com.example.themoviesapp.main.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.themoviesapp.main.presentation.MainViewModel
import com.example.themoviesapp.theme.TheMoviesAppTheme
import com.example.themoviesapp.util.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheMoviesAppTheme {

                Navigation()
            }
        }
    }

}

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.MEDIA_MAIN_SCREEN
    ) {

        composable(route = Route.MEDIA_MAIN_SCREEN) {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ){
                MediaMainScreen(navController = navController)
            }

        }

        composable(route = Route.MEDIA_DETAILS_SCREEN) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(Route.MEDIA_DETAILS_SCREEN)
                }
            }

        }

        composable(route = Route.SEARCH_SCREEN) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(Route.SEARCH_SCREEN)
            }

        }

        composable(route = Route.WATCH_VIDEO_SCREEN) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(Route.WATCH_VIDEO_SCREEN)
            }

        }

        composable(route = Route.SIMILAR_MEDIA_LIST_SCREEN) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(Route.SIMILAR_MEDIA_LIST_SCREEN)
            }

        }
    }


}
