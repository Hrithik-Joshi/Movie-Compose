package com.hrithik.moviecompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.hrithik.moviecompose.ui.navigation.NavigationHost
import com.hrithik.moviecompose.ui.screens.BottomNavigationBar
import com.hrithik.moviecompose.ui.theme.MovieComposeTheme
import com.hrithik.moviecompose.viewModel.MovieListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val movieListViewModel: MovieListViewModel by viewModel()

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
                                    0 -> navController.navigate("movie_list")
                                    1 -> navController.navigate("search")
                                    2 -> navController.navigate("favorites")
                                }
                            }
                        )
                    }
                ) {
                    NavigationHost(navController = navController)
                    BackHandler {
                        // Do nothing to completely disable the back press
                    }
                }
            }
        }
    }
}