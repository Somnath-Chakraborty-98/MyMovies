package com.example.mymoviesapp.ui.fragments

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.mymoviesapp.R
import com.example.mymoviesapp.ui.MainActivity
import com.example.mymoviesapp.ui.MovieViewModel
import com.example.mymoviesapp.util.Constants.Companion.SINGLE_MOVIE_URL
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie_details.*



class movieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    lateinit var viewModel: MovieViewModel
    val args: movieDetailsFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= (activity as MainActivity).viewModel

        val movie = args.movie
        val movieURL = SINGLE_MOVIE_URL+movie.id
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(movieURL)
        }

        fab.setOnClickListener {
            viewModel.saveMovie(movie)
            Snackbar.make(view,"Movie Saved Successfully",Snackbar.LENGTH_SHORT).show()
        }

    }
}