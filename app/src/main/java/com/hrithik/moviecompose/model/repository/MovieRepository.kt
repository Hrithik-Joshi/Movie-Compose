package com.hrithik.moviecompose.model.repository

import com.hrithik.moviecompose.model.api.RetrofitInstance
import com.hrithik.moviecompose.model.entities.Movie

class MovieRepository {

    suspend fun getPopularMovies(apiKey: String): List<Movie> {
        return RetrofitInstance.api.getPopularMovies(apiKey).results
    }
}