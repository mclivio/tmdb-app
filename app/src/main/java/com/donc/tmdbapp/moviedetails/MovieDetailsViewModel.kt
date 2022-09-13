package com.donc.tmdbapp.moviedetails

import androidx.lifecycle.ViewModel
import com.donc.tmdbapp.data.remote.responses.Movie
import com.donc.tmdbapp.repository.MovieRepository
import com.donc.tmdbapp.util.Constants.API_KEY
import com.donc.tmdbapp.util.Constants.LANGUAGE
import com.donc.tmdbapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    suspend fun getMovieInfo(movie_id: Int): Resource<Movie> {
        return repository.getMovie(movie_id, API_KEY, LANGUAGE)
    }
}