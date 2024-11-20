package com.hrithik.moviecompose.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrithik.moviecompose.model.entities.Movie
import com.hrithik.moviecompose.model.repository.MovieRepository
import com.hrithik.moviecompose.viewModel.MovieListViewModel.MovieListViewModelState
import com.hrithik.moviecompose.viewModel.MovieListViewModel.MovieUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    enum class SearchViewModelState {
        INIT,
        IN_PROGRESS,
        SUCCESS_MOVIE_LIST,
        ERROR_MOVIE_LIST
    }

    data class MovieData(
        val movieTitle: String? = null,
        val releaseDate: String? = null,
        val popularity: Int? = null,
        val imageUrl: String? = null,
    )
    data class SearchMovieUIState(
        val movies: List<MovieData> = emptyList(),
        val movieUIState: SearchViewModelState = SearchViewModelState.INIT,
    )

    private val _searchMovieUIState = MutableStateFlow(SearchMovieUIState())
    val searchMovieUIState: StateFlow<SearchMovieUIState> = _searchMovieUIState.asStateFlow()

    fun searchMovies(apiKey: String, query: String) {
        viewModelScope.launch {
            try {
                val response = movieRepository.searchMovies(apiKey, query)
                val movieDataList = response.results.map { movie ->
                    MovieData(
                        movieTitle = movie.title,
                        releaseDate = movie.release_date,
                        popularity = (movie.vote_average?.times(10))?.toInt(),
                        imageUrl = movie.poster_path
                    )
                }
                _searchMovieUIState.update {
                    it.copy(
                        movies = it.movies + movieDataList,
                        movieUIState = SearchViewModelState.SUCCESS_MOVIE_LIST
                    )
                }
            } catch (e: Exception) {
                _movies.value = emptyList()
            }
        }
    }
}