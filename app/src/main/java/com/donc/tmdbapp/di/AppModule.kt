package com.donc.tmdbapp.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /* Aca se define como se proporciona instancias de determinados tipos para que hilt haga inyecciones
    Al instalarse en un singleton, estas dependencias estaran disponibles en toda la aplicacion. */
}