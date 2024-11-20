package com.hrithik.moviecompose.model.repository

import com.hrithik.moviecompose.model.api.RetrofitInstance
import com.hrithik.moviecompose.model.entities.Movie
import com.hrithik.moviecompose.model.entities.MovieResponse

class MovieRepository {

    suspend fun getPopularMovies(apiKey: String, page: Int = 1): MovieResponse {
        return RetrofitInstance.api.getPopularMovies(apiKey, page)
    }

    suspend fun searchMovies(apiKey: String, query: String, page: Int = 1): MovieResponse {
        return RetrofitInstance.api.searchMovies(apiKey, query, page)
    }
}