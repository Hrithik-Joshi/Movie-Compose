package com.hrithik.moviecompose.api

import com.hrithik.moviecompose.models.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

//import retrofit2.http

interface PopularMovieAPI {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key")
        apiKey: String
    ): MovieResponse

}