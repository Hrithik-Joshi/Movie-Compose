package com.hrithik.moviecompose.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hrithik.moviecompose.viewModel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    searchMovieViewModel: SearchViewModel = koinViewModel()
) {
    val searchResults by searchMovieViewModel.searchMovieUIState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(WindowInsets.systemBars.asPaddingValues())
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    if (searchText.isNotEmpty()) {
                        searchMovieViewModel.searchMovies(
                            "c51c823be371778659b3eb1cc37de357",
                            searchText
                        )
                    }
                },
                placeholder = { Text("Search movies...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (searchResults.movieUIState) {
                    SearchViewModel.SearchViewModelState.IN_PROGRESS -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(alignment = Alignment.Center)
                        )
                    }

                    SearchViewModel.SearchViewModelState.ERROR_MOVIE_LIST -> {

                        Text(
                            text = "Failed to load movies. Please try again.",
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )

                    }

                    SearchViewModel.SearchViewModelState.NO_RESULTS -> {
                        Box(modifier = Modifier.align(Alignment.Center)) {
                            Text(
                                text = "No Movies Found",
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    else -> {
                        // Search Results
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                                .padding(bottom = 80.dp)
                        ) {
                            items(searchResults.movies.size) { index ->
                                val movie = searchResults.movies[index]
                                MovieCard(
                                    imageUrl = movie.imageUrl,
                                    likes = movie.popularity,
                                    title = movie.movieTitle,
                                    releaseDate = movie.releaseDate,
                                    onClick =  {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

