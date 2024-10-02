package com.spoelt.moviemate.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.spoelt.moviemate.data.MovieRepositoryImpl
import com.spoelt.moviemate.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.favoritesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "com.spoelt.moviemate.favorites"
)

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideFavoritesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.favoritesDataStore
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        @ApplicationContext context: Context,
        favoritesDataStore: DataStore<Preferences>
    ): MovieRepository = MovieRepositoryImpl(context, favoritesDataStore)
}