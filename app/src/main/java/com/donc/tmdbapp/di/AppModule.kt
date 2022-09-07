package com.donc.tmdbapp.di

import com.donc.tmdbapp.data.remote.MovieApi
import com.donc.tmdbapp.repository.MovieRepository
import com.donc.tmdbapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /* Aca se define como se proporciona instancias de determinados tipos para que hilt haga inyecciones
    Al instalarse en un singleton, estas dependencias estaran disponibles en toda la aplicacion. */
    @Singleton
    @Provides
    fun provideMovieRepository(
        api: MovieApi
    ) = MovieRepository(api)

    @Singleton
    @Provides
    fun provideMovieApi(): MovieApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(MovieApi::class.java)
    }
}