package com.android.movies.repository

import com.android.movies.retrofit.RetrofitService

class MovieListRepository constructor(private val retrofitService: RetrofitService) {
    suspend fun getMovieList(movieName: String, type: String) = retrofitService.getMovieList(movieName, type)
}

