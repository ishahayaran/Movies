package com.android.movies.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.movies.model.MovieListResponse
import com.android.movies.repository.MovieListRepository
import kotlinx.coroutines.*
import java.lang.Exception

class MovieListViewModel constructor(private val movieListRepository: MovieListRepository) : ViewModel() {


    val errorMessage = MutableLiveData<String>()
    val movieListResponse = MutableLiveData<MovieListResponse>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }


    fun getMovieListData(movieName: String,  movieType: String){
        try{


            job = CoroutineScope(Dispatchers.IO).launch {
                val response = movieListRepository.getMovieList(movieName, movieType)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        movieListResponse.postValue(response.body())
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                }
            }
        }
        catch (e: Exception){
            Log.e("TAG", "getMovieListData: ${e.stackTraceToString()}")
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}