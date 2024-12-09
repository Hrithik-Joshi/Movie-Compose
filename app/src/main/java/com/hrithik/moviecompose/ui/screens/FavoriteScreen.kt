package com.hrithik.moviecompose.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hrithik.moviecompose.viewModel.FavoriteListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(favoriteListViewModel: FavoriteListViewModel = koinViewModel()) {

    val movieDetails by favoriteListViewModel.movieDBState.collectAsState()
    val scrollState = rememberLazyGridState()
    Scaffold(
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (movieDetails.favoriteUIState) {
                    FavoriteListViewModel.FavoriteListModelState.IN_PROGRESS -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(alignment = Alignment.Center)
                        )
                    }

                    FavoriteListViewModel.FavoriteListModelState.ERROR_MOVIE_LIST -> {
                        Text(
                            text = "Failed to load movies. Please try again.",
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }

                    FavoriteListViewModel.FavoriteListModelState.SUCCESS_MOVIE_LIST -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            state = scrollState,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(movieDetails.movies.size) { index ->
                                val movie = movieDetails.movies[index]
                                MovieCard(
                                    imageUrl = movie.imageUrl,
                                    likes = movie.popularity,
                                    title = movie.movieTitle,
                                    releaseDate = movie.releaseDate,
                                    onClick = { }
                                )
                            }
                        }
                    }

                    FavoriteListViewModel.FavoriteListModelState.INIT -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(alignment = Alignment.Center)
                        )
                    }

                    FavoriteListViewModel.FavoriteListModelState.NO_RESULTS -> {
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.Center)
                        ) {
                            Text(
                                text = ("Favorite list is empty"),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    }
                }
            }
        },
        topBar = {
            TopAppBar(modifier = Modifier.height(60.dp),
                title = {
                    Text("Favorite Movies")
                },
                actions = {
                    if (movieDetails.favoriteUIState != FavoriteListViewModel.FavoriteListModelState.NO_RESULTS) {
                        Text(
                            modifier = Modifier
                                .clickable {
                                    favoriteListViewModel.clearMovieDB()
                                },
                            text = AnnotatedString("Clear All"),
                        )
                    }
                })
        })
}