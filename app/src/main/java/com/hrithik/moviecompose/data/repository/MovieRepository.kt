package com.hrithik.moviecompose.data.repository

import com.hrithik.moviecompose.data.api.RetrofitInstance
import com.hrithik.moviecompose.data.model.MovieResponse

class MovieRepository {

    suspend fun getPopularMovies(apiKey: String, page: Int = 1): MovieResponse {
        return RetrofitInstance.api.getPopularMovies(apiKey, page)
    }

    suspend fun searchMovies(apiKey: String, query: String, page: Int = 1): MovieResponse {
        return RetrofitInstance.api.searchMovies(apiKey, query, page)
    }
}