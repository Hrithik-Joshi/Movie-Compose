package com.hrithik.moviecompose.data.api

import com.hrithik.moviecompose.data.model.MovieResponse
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

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): MovieResponse
}