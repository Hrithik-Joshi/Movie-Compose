package com.hrithik.moviecompose.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrithik.moviecompose.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteListViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    enum class FavoriteListModelState {
        INIT,
        IN_PROGRESS,
        SUCCESS_MOVIE_LIST,
        ERROR_MOVIE_LIST,
        NO_RESULTS
    }

    data class MovieData(
        val movieId: Int? = null,
        val movieTitle: String? = null,
        val releaseDate: String? = null,
        val popularity: Int? = null,
        val imageUrl: String? = null,
        val overview: String? = null
    )

    data class MovieDBState(
        val movies: List<MovieData> = emptyList(),
        val favoriteUIState: FavoriteListModelState = FavoriteListModelState.INIT,
    )

    private val _movieDBState = MutableStateFlow(MovieDBState())
    val movieDBState: StateFlow<MovieDBState> = _movieDBState.asStateFlow()


    init {
        fetchMoviesFromDB()
    }

    private fun fetchMoviesFromDB() {
        _movieDBState.update {
            it.copy(favoriteUIState = FavoriteListModelState.IN_PROGRESS)
        }
        viewModelScope.launch {
            try {
                val movies = movieRepository.getMovieFromDB()
                val baseImageUrl = "https://image.tmdb.org/t/p/w500/"
                val movieDataList = movies.map { movie ->
                    MovieData(
                        movieId = movie.id,
                        movieTitle = movie.title,
                        releaseDate = movie.release_date,
                        popularity = (movie.vote_average?.times(10))?.toInt(),
                        imageUrl = movie.poster_path?.let { baseImageUrl + it },
                        overview = movie.overview
                    )
                }
                _movieDBState.update {
                    it.copy(
                        movies = it.movies + movieDataList,
                        favoriteUIState = FavoriteListModelState.SUCCESS_MOVIE_LIST
                    )
                }
            } catch (e: Exception) {
                _movieDBState.update {
                    it.copy(favoriteUIState = FavoriteListModelState.ERROR_MOVIE_LIST)
                }
            }
        }
    }

    fun clearMovieDB() {
        viewModelScope.launch {
            try {
                movieRepository.clearMovieInDB()
            }catch (e: Exception){
                print(message = e.message)
            }
        }
    }
}