package com.hrithik.moviecompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hrithik.moviecompose.ui.screens.FavoriteScreen
import com.hrithik.moviecompose.ui.screens.MovieCardList
import com.hrithik.moviecompose.ui.screens.SearchScreen

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "movie_list") {
        composable("movie_list") {
            MovieCardList()
        }
        composable("search") {
            SearchScreen()
        }
        composable("favorites") {
            FavoriteScreen()
        }
    }
}