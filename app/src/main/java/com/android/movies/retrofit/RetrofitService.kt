package com.android.movies.retrofit

import com.android.movies.model.MovieListResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("?apikey=b9bd48a6")
    suspend fun getMovieList(@Query("s") movieName: String, @Query("type") movieType: String): Response<MovieListResponse>


    companion object{
        var retrofitService: RetrofitService? = null

        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        fun getInstance() : RetrofitService {
            if(retrofitService == null){
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://www.omdbapi.com/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}