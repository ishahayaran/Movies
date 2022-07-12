package com.android.movies.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.movies.R
import com.android.movies.model.MovieListDataModel
import java.lang.Exception

import android.widget.TextView
import com.bumptech.glide.Glide


class MovieAdapter(private val movieModels: List<MovieListDataModel>): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_movies, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        try {
            val item = movieModels[position]
            holder.title.text = item.Title
            val imageUrl = item.Poster
            Glide.with(holder.itemView.context).load(imageUrl).into(holder.moviePoster)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return movieModels.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var moviePoster: ImageView = itemView.findViewById(R.id.poster)
        var title: TextView = itemView.findViewById(R.id.title)
    }
}