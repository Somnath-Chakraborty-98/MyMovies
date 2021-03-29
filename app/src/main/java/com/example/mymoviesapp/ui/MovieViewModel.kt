package com.example.mymoviesapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoviesapp.MovieApplication
import com.example.mymoviesapp.dataclass.popularMovies.PopularMovies
import com.example.mymoviesapp.dataclass.popularMovies.Result
import com.example.mymoviesapp.repository.MovieRepository
import com.example.mymoviesapp.util.Resources
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MovieViewModel(
    app: Application,
    val movieRepository: MovieRepository
    ) : AndroidViewModel(app) {

    val popularMovies: MutableLiveData<Resources<PopularMovies>> = MutableLiveData()
    var popularMoviePage = 1
    var popularMoviesResponse: PopularMovies? = null

    val searchMovies: MutableLiveData<Resources<PopularMovies>> = MutableLiveData()
    var searchrMoviePage = 1
    var SearchMoviesResponse: PopularMovies? = null

    var newSearchQuery:String? = null
    var oldSearchQuery:String? = null

    init {
        getPopularMovies()
    }

    fun getPopularMovies() = viewModelScope.launch {
        safePopularMovieCall()
    }

    fun searchMovies(searchQuery: String) = viewModelScope.launch {
        safeSearchMovieCall(searchQuery)
    }

    private fun handlePopularMovieResponse(response: Response<PopularMovies>): Resources<PopularMovies>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                popularMoviePage++
                if(popularMoviesResponse == null){
                    popularMoviesResponse = resultResponse
                } else {
                    val oldMovies = popularMoviesResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return Resources.Success(popularMoviesResponse?: resultResponse)
            }
        }

        return  Resources.Error(response.message())
    }

    private fun handleSearchMovieResponse(response: Response<PopularMovies>): Resources<PopularMovies>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                searchrMoviePage++
                if(SearchMoviesResponse == null || newSearchQuery != oldSearchQuery){
                    searchrMoviePage = 1
                    oldSearchQuery = newSearchQuery
                    SearchMoviesResponse = resultResponse
                } else {
                    val oldMovies = SearchMoviesResponse?.results
                    val newMovies = resultResponse.results
                    oldMovies?.addAll(newMovies)
                }
                return Resources.Success(SearchMoviesResponse?: resultResponse)
            }
        }

        return  Resources.Error(response.message())
    }

    fun saveMovie(result: Result) = viewModelScope.launch {
        movieRepository.upsert(result)
    }

    fun getSavedMovies() = movieRepository.getSavedMovies()

    fun deleteMovie(result: Result) = viewModelScope.launch {
        movieRepository.deleteMovie(result)
    }

    private suspend fun safeSearchMovieCall(searchQuery: String){
        newSearchQuery = searchQuery
        searchMovies.postValue(Resources.Loading())
        try {
            if(hasInternetConnection()){
                val response = movieRepository.searchMovie(searchQuery,searchrMoviePage)
                searchMovies.postValue(handleSearchMovieResponse(response))
            } else {
                searchMovies.postValue(Resources.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when(t){
                is IOException -> searchMovies.postValue(Resources.Error("Network Error"))
                else -> searchMovies.postValue(Resources.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safePopularMovieCall(){
        popularMovies.postValue(Resources.Loading())
        try {
            if(hasInternetConnection()){
                val response = movieRepository.getPopularMovies(popularMoviePage)
                popularMovies.postValue(handlePopularMovieResponse(response))
            } else {
                popularMovies.postValue(Resources.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when(t){
                is IOException -> popularMovies.postValue(Resources.Error("Network Error"))
                else -> popularMovies.postValue(Resources.Error("Conversion Error"))
            }
        }
    }


    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<MovieApplication>().getSystemService(
          Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}