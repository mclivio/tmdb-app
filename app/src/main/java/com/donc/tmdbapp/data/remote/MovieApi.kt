package com.donc.tmdbapp.data.remote

import com.donc.tmdbapp.data.remote.responses.Movie
import com.donc.tmdbapp.data.remote.responses.MovieList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    //Clase Usada por Retrofit para acceder a las rutas de la API

    @GET("movie/popular")
    suspend fun getMovieList(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): MovieList

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Movie
}