package com.example.mymoviesapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymoviesapp.repository.MovieRepository

class MovieViewModelProviderfactory(
    val app: Application,
    val movieRepository: MovieRepository
    ):  ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieViewModel(app, movieRepository) as T
    }
}