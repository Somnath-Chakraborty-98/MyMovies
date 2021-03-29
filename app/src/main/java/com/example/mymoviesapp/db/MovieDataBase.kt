package com.example.mymoviesapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mymoviesapp.dataclass.popularMovies.Result



@Database(
        entities = [Result::class],
        version = 1
)
abstract class MovieDataBase : RoomDatabase(){

    abstract  fun getMovieDao(): MovieDao

    companion object{
        @Volatile
        private var instance: MovieDataBase? = null
        private val Lock = Any()

        operator fun invoke(context: Context) = instance?: synchronized(Lock){
            instance?: createDataBase(context).also{ instance=it }
        }

        private fun createDataBase(context: Context)= Room.databaseBuilder(
                context.applicationContext,
                MovieDataBase::class.java,
                "movie_db.db"
        ).build()
    }

}