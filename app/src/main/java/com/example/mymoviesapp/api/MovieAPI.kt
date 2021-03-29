package com.example.mymoviesapp.api

import com.example.mymoviesapp.dataclass.popularMovies.PopularMovies
import com.example.mymoviesapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
            @Query("api_key")
            apiKey: String = API_KEY,
            @Query("page")
            pageNumber: Int = 1

    ): Response<PopularMovies>

    @GET("/3/search/movie")
    suspend fun getSearchMovies(
            @Query("api_key")
            apiKey: String = API_KEY,
            @Query("query")
            searchQuery: String,
            @Query("page")
            pageNumber: Int = 1

    ): Response<PopularMovies>



}