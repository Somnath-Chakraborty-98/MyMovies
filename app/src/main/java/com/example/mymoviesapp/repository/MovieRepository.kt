package com.example.mymoviesapp.repository

import com.example.mymoviesapp.api.RetrofitInstance
import com.example.mymoviesapp.dataclass.popularMovies.Result
import com.example.mymoviesapp.db.MovieDataBase
import com.example.mymoviesapp.util.Constants.Companion.API_KEY

class MovieRepository(val db: MovieDataBase) {

    suspend fun getPopularMovies(page: Int) =
            RetrofitInstance.api.getPopularMovies(API_KEY,page)

    suspend fun searchMovie(searchQyery: String, page: Int)=
            RetrofitInstance.api.getSearchMovies(API_KEY,searchQyery,page)

    suspend fun upsert(result: Result) = db.getMovieDao().upsert(result)

    fun getSavedMovies() = db.getMovieDao().getAllMovies()

    suspend fun deleteMovie(result: Result) = db.getMovieDao().deleteMovie(result)
}