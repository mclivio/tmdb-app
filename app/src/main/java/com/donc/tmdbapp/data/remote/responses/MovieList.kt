package com.donc.tmdbapp.data.remote.responses

data class MovieList(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)