package com.donc.tmdbapp.repository

import com.donc.tmdbapp.data.remote.MovieApi
import com.donc.tmdbapp.data.remote.responses.Movie
import com.donc.tmdbapp.data.remote.responses.MovieList
import com.donc.tmdbapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MovieRepository @Inject constructor(private val api: MovieApi) {
    suspend fun getMovieList(api_key: String, language: String, page: Int): Resource<MovieList> {
        val response = try {
            api.getMovieList(api_key, language, page)
        } catch(e: Exception) {
            return Resource.Error("Ha ocurrido un error inesperado. Intente nuevamente.")
        }
        return Resource.Success(response)
    }
    suspend fun getMovie(movie_id: Int, api_key: String, language: String): Resource<Movie> {
        val response = try {
            api.getMovieDetails(movie_id, api_key, language)
        } catch(e: Exception) {
            return Resource.Error("Ha ocurrido un error inesperado. Intente nuevamente.")
        }
        return Resource.Success(response)
    }
}