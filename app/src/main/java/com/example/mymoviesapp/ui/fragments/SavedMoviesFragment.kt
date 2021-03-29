package com.example.mymoviesapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoviesapp.R
import com.example.mymoviesapp.adapters.MoviesAdapter
import com.example.mymoviesapp.ui.MainActivity
import com.example.mymoviesapp.ui.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_popular.*
import kotlinx.android.synthetic.main.fragment_saved_movies.*


class savedMoviesFragment : Fragment(R.layout.fragment_saved_movies) {

    lateinit var viewModel: MovieViewModel
    lateinit var movieAdapter: MoviesAdapter
    val TAG = "SavedMoviesFragment"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= (activity as MainActivity).viewModel
        setupRecyclerView()

        movieAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
                Log.e(TAG,"Movie Details")
            }
            findNavController().navigate(
                    R.id.action_savedMoviesFragment_to_movieDetailsFragment,
                    bundle
            )
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val movie = movieAdapter.differ.currentList[position]
                viewModel.deleteMovie(movie)
                Snackbar.make(view,"Successfully deleted Movie",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveMovie(movie)
                    }
                    show()
                }

            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedMovies)
        }

        viewModel.getSavedMovies().observe(viewLifecycleOwner, Observer { movies->
            movieAdapter.differ.submitList(movies)
        })
    }

    private fun setupRecyclerView(){
        movieAdapter = MoviesAdapter()
        rvSavedMovies.apply {
            adapter = movieAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
}