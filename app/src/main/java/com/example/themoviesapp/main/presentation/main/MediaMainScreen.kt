package com.example.themoviesapp.main.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.themoviesapp.main.presentation.MainViewModel
import com.example.themoviesapp.main.presentation.home.HomeScreen
import com.example.themoviesapp.main.presentation.media_list_screen.MediaListScreen
import com.example.themoviesapp.theme.font
import com.example.themoviesapp.util.BottomNavRoute
import com.example.themoviesapp.util.Constants

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
)

@Composable
fun MediaMainScreen(
    navController: NavController,
) {
    val navigationItem = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Popular",
            selectedIcon = Icons.Filled.LocalFireDepartment,
            unSelectedIcon = Icons.Outlined.LocalFireDepartment
        ),
        BottomNavigationItem(
            title = "TV Series",
            selectedIcon = Icons.Filled.LiveTv,
            unSelectedIcon = Icons.Outlined.LiveTv
        )
    )

    val selectedItem = rememberSaveable {
        mutableIntStateOf(0)
    }

    val bottomBarNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                navigationItem.forEachIndexed { index, bottomNavigationItem ->
                    NavigationBarItem(
                        selected = index == selectedItem.intValue,
                        onClick = {
                            selectedItem.intValue = index
                            when (selectedItem.intValue) {
                                0 -> bottomBarNavController.navigate(BottomNavRoute.MEDIA_HOME_SCREEN)

                                1 -> bottomBarNavController.navigate("${BottomNavRoute.MEDIA_LIST_SCREEN}?type=${Constants.popularScreen}")

                                2 -> bottomBarNavController.navigate("${BottomNavRoute.MEDIA_LIST_SCREEN}?type=${Constants.tvSeriesScreen}")

                            }

                        },
                        icon = {
                            Icon(
                                imageVector = if (selectedItem.intValue == index) bottomNavigationItem.selectedIcon
                                else bottomNavigationItem.unSelectedIcon,
                                contentDescription = bottomNavigationItem.title,
                                tint = MaterialTheme.colorScheme.onBackground

                            )
                        },
                        label = {
                            Text(
                                text = bottomNavigationItem.title,
                                fontFamily = font,
                                color = MaterialTheme.colorScheme.onBackground

                            )
                        }
                    )
                }
            }
        }

    ) { paddingValues ->
        BottomNavigationScreens(
            modifier = Modifier.padding(
                bottom = paddingValues.calculateBottomPadding()
            ),
            selectedItem = selectedItem,
            navController = navController,
            bottomBarNavController = bottomBarNavController
        )
    }

}

@Composable
fun BottomNavigationScreens(
    selectedItem: MutableState<Int>,
    navController: NavController,
    bottomBarNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        val mainViewModel = hiltViewModel<MainViewModel>()
        val mainUiState = mainViewModel.mainUiState.collectAsState()
        NavHost(
            navController = bottomBarNavController,
            startDestination = BottomNavRoute.MEDIA_HOME_SCREEN
        ) {
            composable(
                route = BottomNavRoute.MEDIA_HOME_SCREEN
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    HomeScreen(
                        navHostController = navController,
                        bottomNavController = bottomBarNavController,
                        mainUiState = mainUiState.value)
                }

            }

            composable(
                route = "${BottomNavRoute.MEDIA_LIST_SCREEN}?type={type}",
                arguments = listOf(
                    navArgument("type") {
                        type = NavType.StringType
                    }
                )
            ) {
                val type = it.arguments?.getString("type")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    MediaListScreen(
                        mainUiState = mainUiState.value,
                        navBackStackEntry = it
                    )
                }

            }
        }
    }

}