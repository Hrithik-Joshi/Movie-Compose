package com.hrithik.moviecompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hrithik.moviecompose.viewModel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    searchMovieViewModel: SearchViewModel = koinViewModel()
) {
    val searchResults by searchMovieViewModel.movies.collectAsState()
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search Bar
        TextField(
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
            singleLine = true
        )

        // Search Results
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(searchResults) { movie ->
                MovieCard(
                    imageUrl = movie.poster_path,
                    likes = movie.vote_average?.toInt(),
                    title = movie.title,
                    releaseDate = movie.release_date
                )
            }
        }
    }
}

