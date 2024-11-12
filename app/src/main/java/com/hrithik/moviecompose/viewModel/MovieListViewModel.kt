package com.hrithik.moviecompose.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrithik.moviecompose.model.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieListViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    enum class MovieListViewModelState {
        INIT,
        IN_PROGRESS,
        SUCCESS_MOVIE_LIST,
        ERROR_MOVIE_LIST
    }

    data class MovieData(
        val movieTitle: String? = null,
        val releaseDate: String? = null,
        val popularity: Double? = null,
        val imageUrl: String? = null,
    )

    data class MovieUIState(
        val movies: List<MovieData> = emptyList(),
        val movieUIState: MovieListViewModelState = MovieListViewModelState.INIT
    )

    private val _movieListUIState = MutableStateFlow(MovieUIState())
    val movieListUIState: StateFlow<MovieUIState> = _movieListUIState.asStateFlow()

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        _movieListUIState.update { it.copy(movieUIState = MovieListViewModelState.IN_PROGRESS) }
        viewModelScope.launch {
            try {
                val movies = movieRepository.getPopularMovies("c51c823be371778659b3eb1cc37de357")
                val movieDataList = movies.map { movie ->
                    MovieData(
                        movieTitle = movie.title,
                        releaseDate = movie.release_date,
                        popularity = movie.popularity,
                        imageUrl = movie.poster_path
                    )
                }
                _movieListUIState.update {
                    it.copy(
                        movies = movieDataList,
                        movieUIState = MovieListViewModelState.SUCCESS_MOVIE_LIST
                    )
                }
            } catch (e: Exception) {
                _movieListUIState.update {
                    it.copy(movieUIState = MovieListViewModelState.ERROR_MOVIE_LIST)
                }
            }
        }
    }
}