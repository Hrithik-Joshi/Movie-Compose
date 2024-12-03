package com.hrithik.moviecompose.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_table")
data class Movie(
    @PrimaryKey
    val id: Int?,
    val title: String?,
    val release_date: String?,
    val overview: String?,
    val poster_path: String?,
    val vote_average: Double?,
)