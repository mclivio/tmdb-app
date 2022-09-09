package com.donc.tmdbapp.movielist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donc.tmdbapp.models.MovieListEntry
import com.donc.tmdbapp.repository.MovieRepository
import com.donc.tmdbapp.util.Constants.API_KEY
import com.donc.tmdbapp.util.Constants.LANGUAGE
import com.donc.tmdbapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    private var curPage = 1
    var moviesList = mutableStateOf<List<MovieListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init{
        loadMoviesPaginated()
    }

    fun loadMoviesPaginated(){
        viewModelScope.launch{
            isLoading.value = true
            val result = repository.getMovieList(API_KEY, LANGUAGE, curPage)
            when(result){
                is Resource.Success -> {
                    endReached.value = curPage  >= result.data!!.total_pages
                    val moviesEntries = result.data.results.mapIndexed{ _, entry ->
                        MovieListEntry(
                            entry.poster_path,
                            entry.title.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            },
                            entry.id
                        )
                    }
                    curPage++
                    loadError.value = ""
                    isLoading.value = false
                    moviesList.value+= moviesEntries
                }
                is Resource.Error-> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> throw IllegalArgumentException("Illegal Result")
            }
        }
    }
}