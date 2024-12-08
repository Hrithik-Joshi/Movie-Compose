package com.hrithik.moviecompose.data.repository

import android.content.Context
import com.hrithik.moviecompose.data.api.RetrofitInstance
import com.hrithik.moviecompose.data.db.MovieComposeDatabase
import com.hrithik.moviecompose.data.db.MovieDao
import com.hrithik.moviecompose.data.model.Movie
import com.hrithik.moviecompose.data.model.MovieResponse
import kotlinx.coroutines.flow.Flow

class MovieRepository(context: Context) {

    private val db = MovieComposeDatabase.getInstance(context)
    private val movieDao: MovieDao = db.movieDao

    suspend fun getPopularMovies(apiKey: String, page: Int = 1): MovieResponse {
        return RetrofitInstance.api.getPopularMovies(apiKey, page)
    }

    suspend fun searchMovies(apiKey: String, query: String, page: Int = 1): MovieResponse {
        return RetrofitInstance.api.searchMovies(apiKey, query, page)
    }

    suspend fun getMovieFromDB(): List<Movie>{
        return movieDao.getAllMoviesInDB()
    }

    suspend fun insertMovieToDB(movie: Movie){
        movieDao.insertMovie(movie)
    }

    suspend fun deleteMovieFromDB(movieId: Int){
        movieDao.deleteMovieById(movieId)
    }

    suspend fun findMovieInDB(movieId: Int): Boolean {
       val movie = movieDao.findMovieById(movieId)
        return movie != null
    }

    fun getFavoritesCount(): Flow<Int> {
        return movieDao.getFavoritesCount()
    }

}