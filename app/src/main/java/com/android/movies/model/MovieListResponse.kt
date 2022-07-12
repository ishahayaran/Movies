package com.android.movies.model


data class MovieListResponse (var Search: List<Search>, var Response: String)

data class Search(var Title: String, var Year: Int, var imdbID: String, var Type: String, var Poster: String)
