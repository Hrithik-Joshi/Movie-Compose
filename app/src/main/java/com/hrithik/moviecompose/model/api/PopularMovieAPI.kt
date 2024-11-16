package com.hrithik.moviecompose.model.api

import com.hrithik.moviecompose.model.entities.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

//import retrofit2.http

interface PopularMovieAPI {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key")
        apiKey: String,
        @Query("page") page: Int = 1
    ): MovieResponse

}