package com.example.mymoviesapp.dataclass.popularMovies

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(
    tableName = "movies"
)
data class Result(
        @PrimaryKey
        val id: Int,
        val original_title: String,
        val overview: String,
        val poster_path: String,
        val release_date: String,
        val vote_average: Double

): Serializable