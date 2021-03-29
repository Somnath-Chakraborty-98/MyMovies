package com.example.mymoviesapp.ui.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoviesapp.R
import com.example.mymoviesapp.adapters.MoviesAdapter
import com.example.mymoviesapp.ui.MainActivity
import com.example.mymoviesapp.ui.MovieViewModel
import com.example.mymoviesapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.mymoviesapp.util.Resources
import kotlinx.android.synthetic.main.fragment_popular.*

class popularFragment : Fragment(R.layout.fragment_popular) {

    lateinit var viewModel: MovieViewModel
    lateinit var movieAdapter: MoviesAdapter

    val TAG = "PopularFragment"

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
                    R.id.action_popularFragment_to_movieDetailsFragment,
                    bundle
            )
        }

        viewModel.popularMovies.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resources.Success ->{
                    hideProgressBar()
                    Log.e(TAG,"Movies Loaded")
                    response.data?.let {movieResponse->
                       movieAdapter.differ.submitList(movieResponse.results.toList())
                        val totalPages = movieResponse.total_results / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.popularMoviePage == totalPages
                        if (isLastPage){
                            rvPopularMovies.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resources.Error->{
                    hideProgressBar()
                    response.message?.let { message->
                        Log.e(TAG,"An error occurred: $message")
                        Toast.makeText(activity,"An error occurred: $message",Toast.LENGTH_SHORT).show()
                    }
                }
                is Resources.Loading-> {
                    showProgressBar()
                    Log.e(TAG,"Movies Loading....")
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getPopularMovies()
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView(){
        movieAdapter = MoviesAdapter()
        rvPopularMovies.apply {
            adapter = movieAdapter
            layoutManager= LinearLayoutManager(activity)
            addOnScrollListener(this@popularFragment.scrollListener)
        }
    }
}