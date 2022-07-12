package com.android.movies.view.activities

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.movies.R
import com.android.movies.databinding.ActivityMainBinding
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.movies.factory.MovieListFactory
import com.android.movies.model.MovieListDataModel
import com.android.movies.repository.MovieListRepository
import com.android.movies.retrofit.RetrofitService
import com.android.movies.utils.ConnectivityReceiver
import com.android.movies.view.adapter.MovieAdapter
import com.android.movies.viewmodel.MovieListViewModel
import java.util.ArrayList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var movieListViewModel: MovieListViewModel
    lateinit var retrofitService: RetrofitService
    val list: MutableList<MovieListDataModel> = ArrayList()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofitService = RetrofitService.getInstance()

        layoutManager = LinearLayoutManager(this)

        binding.searchMovie.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val isConnected: Boolean = ConnectivityReceiver().isConnected(applicationContext)
                if (!isConnected) {
                    Toast.makeText(
                        applicationContext,
                        "You don't have internet connection.",
                        Toast.LENGTH_LONG
                    ).show()

                } else {
                    callSearchMovie()
                }
                return@OnEditorActionListener true
            }
            false
        })

    }

    private fun callSearchMovie() {
        list.clear()
        val mainRepository = MovieListRepository(retrofitService)
        movieListViewModel =
            ViewModelProvider(this, MovieListFactory(mainRepository)).get(
                MovieListViewModel::class.java
            )

        movieListViewModel.getMovieListData(binding.searchMovie.text.toString(), "movie")
        createDialog()
        searchMovies()

        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }


    private fun searchMovies() {
        movieListViewModel.movieListResponse.observe(this) {
            try {
                if (it.Response == "True") {
                    val movieData = it.Search
                    if(dialog.isShowing)
                        dialog.cancel()

                    if (movieData.isNotEmpty()) {

                        for (i in 0 until movieData.size) {
                            val data = movieData[i]
                            if (data.Poster != "N/A") {
                                val dataModel = MovieListDataModel(
                                    data.Title,
                                    data.Poster
                                )

                                list.add(dataModel)
                            }
                        }

                        // added data from arraylist to adapter class.
                        binding.movieList.adapter = MovieAdapter(list)

                        // setting grid layout manager to implement grid view.
                        // in this method '2' represents number of columns to be displayed in grid view.
                        val layoutManager = GridLayoutManager(this, 2)

                        // at last set adapter to recycler view.
                        binding.movieList.layoutManager = layoutManager

                        binding.movieList.visibility = View.VISIBLE
                        binding.notFound.visibility = View.GONE
                    }

                } else {
                    if(dialog.isShowing)
                        dialog.cancel()
                    binding.movieList.visibility = View.GONE
                    binding.notFound.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                Log.e("TAG", "error: ${e.stackTraceToString()}")
                if(dialog.isShowing)
                    dialog.cancel()
            }
        }
    }

    private fun createDialog() {
        dialog = Dialog(this)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.loading_bar)
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        if(binding.searchMovie.text.isNotEmpty()) {
            list.clear()
            val mainRepository = MovieListRepository(retrofitService)

            movieListViewModel =
                ViewModelProvider(this, MovieListFactory(mainRepository)).get(
                    MovieListViewModel::class.java
                )

            movieListViewModel.getMovieListData(binding.searchMovie.text.toString(), "movie")
            createDialog()
            searchMovies()
        }
    }
}