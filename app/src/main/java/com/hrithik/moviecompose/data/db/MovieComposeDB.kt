package com.hrithik.moviecompose.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hrithik.moviecompose.data.model.Movie

@Database(entities = [Movie::class], exportSchema = false, version = 1)
abstract class MovieComposeDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao

    //define a static singleton instance of this db class
    companion object{

        // prevents any possible race conditions in multithreading
        @Volatile
        private var INSTANCE: MovieComposeDatabase? = null

        fun getInstance(context: Context) {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieComposeDatabase::class.java,
                        "movie_compose_db"
                    ).build()
                }
            }
        }
    }

}