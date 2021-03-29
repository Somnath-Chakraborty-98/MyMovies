package com.example.mymoviesapp.dataclass.popularMovies

data class PopularMovies(
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)