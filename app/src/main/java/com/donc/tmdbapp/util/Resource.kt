package com.donc.tmdbapp.util

sealed class Resource<T>(val data: T? = null, val message: String? = null){
    /*Clase Usada para modelar los estados de una respuesta
    DOC: - https://proandroiddev.com/modeling-retrofit-responses-with-sealed-classes-and-coroutines-9d6302077dfe
         - https://medium.com/codex/kotlin-sealed-classes-for-better-handling-of-api-response-6aa1fbd23c76 */
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
