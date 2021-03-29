package com.example.mymoviesapp.ui

import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mymoviesapp.R
import com.example.mymoviesapp.db.MovieDataBase
import com.example.mymoviesapp.repository.MovieRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var viewModel: MovieViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val movieRepository = MovieRepository(MovieDataBase(this))
        val viewModelProviderfactory = MovieViewModelProviderfactory(application, movieRepository)
        viewModel = ViewModelProvider(this, viewModelProviderfactory).get(MovieViewModel::class.java)
        bottomNavigationView.setupWithNavController(moviesNavHostFragment.findNavController())
    }

}