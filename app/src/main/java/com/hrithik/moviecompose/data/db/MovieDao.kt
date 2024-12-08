package com.hrithik.moviecompose.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hrithik.moviecompose.data.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Query("SELECT * FROM movies_table")
    suspend fun getAllMoviesInDB(): List<Movie>

    @Query("DELETE FROM movies_table WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int)

    @Query("SELECT * FROM movies_table WHERE id = :movieId LIMIT 1")
    suspend fun findMovieById(movieId: Int): Movie?

    @Query("SELECT COUNT(*) FROM movies_table")
    fun getFavoritesCount(): Flow<Int>
}