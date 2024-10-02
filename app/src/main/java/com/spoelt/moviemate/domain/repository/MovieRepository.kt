package com.spoelt.moviemate.domain.repository

import com.spoelt.moviemate.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MovieRepository {
    val cachedStaffPicks: StateFlow<List<Movie>?>
    val cachedMovies: StateFlow<List<Movie>?>

    suspend fun getAllMovies(): Flow<List<Movie>?>

    suspend fun getFavorites(): Flow<List<Movie>?>

    suspend fun getStaffPicks(): Flow<List<Movie>?>

    suspend fun getFavoriteIds(): Flow<Set<Int>?>

    suspend fun getMovieDetail(id: Int): Flow<Movie?>

    suspend fun toggleFavorite(id: Int)
}