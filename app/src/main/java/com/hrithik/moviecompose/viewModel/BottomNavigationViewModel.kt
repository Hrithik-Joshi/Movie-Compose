package com.hrithik.moviecompose.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrithik.moviecompose.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BottomNavigationViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private val _favoritesCount = MutableStateFlow(0)
    val favoritesCount: StateFlow<Int> = _favoritesCount

    init {
        viewModelScope.launch {
            movieRepository.getFavoritesCount().collect { count ->
                _favoritesCount.value = count
            }
        }
    }

}