package com.example.mymoviesapp.dataclass.singleMovie

data class SingleMovieDetails(
    val budget: Int,
    val id: Int,
    val original_title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val title: String,
    val vote_average: Double
)