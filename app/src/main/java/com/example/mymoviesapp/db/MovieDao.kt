package com.example.mymoviesapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mymoviesapp.dataclass.popularMovies.Result


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(result: Result): Long

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Result>>

    @Delete
    suspend fun deleteMovie(result: Result)
}