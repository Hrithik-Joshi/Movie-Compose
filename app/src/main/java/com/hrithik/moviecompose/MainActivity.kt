package com.hrithik.moviecompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hrithik.moviecompose.ui.navigation.NavigationHost
import com.hrithik.moviecompose.ui.screens.BottomNavigationBar
import com.hrithik.moviecompose.ui.theme.MovieComposeTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieComposeTheme {
                val navController = rememberNavController()
                var selectedIconIndex by remember { mutableIntStateOf(0) }
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            selectedIndex = selectedIconIndex,
                            onItemSelected = { index ->
                                selectedIconIndex = index
                                when (index) {
                                    0 -> navController.navigate("movie_list") {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        restoreState = true
                                        launchSingleTop = true
                                    }

                                    1 -> navController.navigate("search") {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        restoreState = true
                                        launchSingleTop = true
                                    }

                                    2 -> navController.navigate("favorites") {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        restoreState = true
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavigationHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                    BackHandler {
                        // Do nothing to completely disable the back press
                    }
                }
            }
        }
    }
}