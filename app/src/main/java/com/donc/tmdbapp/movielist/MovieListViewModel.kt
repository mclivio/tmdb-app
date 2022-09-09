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

    private var cachedMoviesList = listOf<MovieListEntry>()
    private var isSearchStarting = true //if search field is empty
    var isSearching = mutableStateOf(false) //if we are showing search results

    init{
        loadMoviesPaginated()
    }

    fun loadMoviesPaginated(){
        viewModelScope.launch{
            isLoading.value = true
            when(val result = repository.getMovieList(API_KEY, LANGUAGE, curPage)){
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
    fun searchMoviesList(query: String) {
        val listToSearch = if(isSearchStarting) { //Si recien inicio el programa y el campo esta vacio
            moviesList.value                      //voy a buscar dentro de la lista en pantalla
        } else {
            cachedMoviesList                       //Sino dentro de mi lista de resultados
        }
        viewModelScope.launch(Dispatchers.Default){
            //Since we are searching in a potentially long list i don't want it to execute
            //in the main thread as it could be quite CPU-heavy.
            //Dispatchers.Default supports tasks equal to CPU cores amount -> CPU-heavy single tasks like image/video conversion.
            //Dispatchers.IO runs by default on 64 threads -> writing/reading files or API connections.
            //Dispatchers.Main runs on main thread -> Updating UI
            //Dispatchers.Unconfined isn't confined to a specific thread -> tasks that don't consume CPU time or update shared data (UI) which is confined to a specific thread (Main)
            if (query.isEmpty()){   //Si borro el campo y esta vacio, tengo que restaurar mi lista completa y cambiar los estados
                moviesList.value = cachedMoviesList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter{
                it.title.contains(query.trim(), ignoreCase = true)
            }
            if(isSearchStarting){   //Si estoy realizando una busqueda tengo que hacer un respaldo
                cachedMoviesList = moviesList.value
                isSearchStarting = false
            }
            moviesList.value = results
            isSearching.value = true
        }
    }
}