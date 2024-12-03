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
        val popularity: Int? = null, // Converted to percentage
        val imageUrl: String? = null,
        val overview: String? = null
    )

    data class MovieUIState(
        val movies: List<MovieData> = emptyList(),
        val movieUIState: MovieListViewModelState = MovieListViewModelState.INIT,
        val currentPage: Int = 1,
        val isLoadingNextPage: Boolean = false
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
                val response = movieRepository.getPopularMovies("c51c823be371778659b3eb1cc37de357", 1)
                val baseImageUrl = "https://image.tmdb.org/t/p/w500/"
                val movieDataList = response.results.map { movie ->
                    MovieData(
                        movieTitle = movie.title,
                        releaseDate = movie.release_date,
                        popularity = (movie.vote_average?.times(10))?.toInt(),
                        imageUrl = movie.poster_path?.let { baseImageUrl + it },
                        overview = movie.overview
                    )
                }
                _movieListUIState.update {
                    it.copy(
                        movies = it.movies + movieDataList,
                        movieUIState = MovieListViewModelState.SUCCESS_MOVIE_LIST,
                        currentPage = response.page,
                    )
                }
            } catch (e: Exception) {
                _movieListUIState.update {
                    it.copy(movieUIState = MovieListViewModelState.ERROR_MOVIE_LIST)
                }
            }
        }
    }

    fun loadNextPage() {
        val currentPage = _movieListUIState.value.currentPage
        _movieListUIState.update { it.copy(isLoadingNextPage = true) }
        viewModelScope.launch {
            try {
                val movieResponse = movieRepository.getPopularMovies("c51c823be371778659b3eb1cc37de357", currentPage + 1)
                val baseImageUrl = "https://image.tmdb.org/t/p/w500/"
                val newMovies = movieResponse.results.map { movie ->
                    MovieData(
                        movieTitle = movie.title,
                        releaseDate = movie.release_date,
                        popularity = (movie.vote_average?.times(10))?.toInt(),
                        imageUrl = movie.poster_path?.let { baseImageUrl + it },
                        overview = movie.overview
                    )
                }
                _movieListUIState.update {
                    it.copy(
                        movies = it.movies + newMovies,
                        currentPage = currentPage + 1,
                        isLoadingNextPage = false
                    )
                }
            } catch (e: Exception) {
                _movieListUIState.update { it.copy(isLoadingNextPage = false) }
            }
        }
    }
}
