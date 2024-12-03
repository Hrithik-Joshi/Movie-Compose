package com.hrithik.moviecompose.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hrithik.moviecompose.data.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Query("SELECT * FROM movies_table")
    suspend fun getAllMoviesInDB(): List<Movie>

    @Query("DELETE FROM movies_table WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int)
}