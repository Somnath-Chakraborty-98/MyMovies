package com.example.mymoviesapp.adapters

import android.view.LayoutInflater
import com.example.mymoviesapp.util.Constants.Companion.IMAGE_BASE_URL
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymoviesapp.R
import com.example.mymoviesapp.dataclass.popularMovies.Result
import kotlinx.android.synthetic.main.item_movie_preview.view.*

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    inner class MoviesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    private val differCallback = object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
           return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_movie_preview,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Result) ->  Unit)? = null

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = differ.currentList[position]
        val moviePosterUrl: String = IMAGE_BASE_URL+movie?.poster_path
        holder.itemView.apply {
            Glide.with(this).load(moviePosterUrl).into(movieImage)
            movieTitle.text = movie.original_title
            movieRating.text = movie.vote_average.toString()
            movieReleasedate.text = movie.release_date


            setOnClickListener {
                onItemClickListener?.let { it(movie) }
            }
        }
    }


    fun setOnItemClickListener(listener: (Result) -> Unit){
        onItemClickListener = listener
    }
}