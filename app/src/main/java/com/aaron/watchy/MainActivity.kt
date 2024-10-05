package com.aaron.watchy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aaron.watchy.main.presentation.MainScreen
import com.aaron.watchy.main.presentation.MainViewModel
import com.aaron.watchy.main.presentation.main_media_list.MainMediaListScreen
import com.aaron.watchy.ui.theme.WatchyTheme
import com.aaron.watchy.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WatchyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val mainViewModel = hiltViewModel<MainViewModel>()

                    val mainState = mainViewModel.mainState.collectAsState().value

                    val mainNavController = rememberNavController()

                    NavHost(
                        navController = mainNavController,
                        startDestination = Screen.Main.route
                    ) {
                        composable(Screen.Main.route) {
                            MainScreen(
                                mainNavController = mainNavController,
                                mainState = mainState,
                                onEvent = mainViewModel::event
                            )
                        }
                        composable(Screen.Trending.route) {
                            MainMediaListScreen(
                                route = Screen.Trending.route,
                                mainNavController = mainNavController,
                                mainState = mainState,
                                onEvent = mainViewModel::event
                            )
                        }
                        composable(Screen.Movies.route) {
                            MainMediaListScreen(
                                route = Screen.Movies.route,
                                mainNavController = mainNavController,
                                mainState = mainState,
                                onEvent = mainViewModel::event
                            )
                        }
                        composable(Screen.Tv.route) {
                            MainMediaListScreen(
                                route = Screen.Tv.route,
                                mainNavController = mainNavController,
                                mainState = mainState,
                                onEvent = mainViewModel::event
                            )
                        }
                    }
                }
            }
        }
    }
}