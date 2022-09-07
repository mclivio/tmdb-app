package com.donc.tmdbapp.repository

import com.donc.tmdbapp.data.remote.MovieApi
import com.donc.tmdbapp.data.remote.responses.Movie
import com.donc.tmdbapp.data.remote.responses.MovieList
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MovieRepository @Inject constructor(private val api: MovieApi) {
    suspend fun getMovieList(api_key: String, language: String, page: Int): MovieList {
        val response = try {
            api.getMovieList(api_key, language, page)
        } catch(e: Exception) {
           //TODO: Aca iria el mensaje de error de la reponse. Implementar una clase para manejar las respuestas
        }
        return response as MovieList
    }
    suspend fun getMovie(movie_id: Int, api_key: String, language: String): Movie {
        val response = try {
            api.getMovieDetails(movie_id, api_key, language)
        } catch(e: Exception) {
            //Idem getMovieList
        }
        return response as Movie
    }
}