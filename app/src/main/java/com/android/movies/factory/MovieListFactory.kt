package com.android.movies.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.movies.repository.MovieListRepository
import com.android.movies.viewmodel.MovieListViewModel

class MovieListFactory constructor(private val repository: MovieListRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MovieListViewModel::class.java)) {
            MovieListViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}