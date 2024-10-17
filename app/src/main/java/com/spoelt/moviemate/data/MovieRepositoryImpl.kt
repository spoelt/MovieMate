package com.spoelt.moviemate.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.spoelt.moviemate.domain.model.Movie
import com.spoelt.moviemate.domain.repository.MovieRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val favoritesDataStore: DataStore<Preferences>
) : MovieRepository {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val _cachedMovies = MutableStateFlow<List<Movie>?>(null)
    override val cachedMovies = _cachedMovies.asStateFlow()

    private val _cachedStaffPicks = MutableStateFlow<List<Movie>?>(null)
    override val cachedStaffPicks = _cachedStaffPicks.asStateFlow()

    override suspend fun getAllMovies(): Flow<List<Movie>> = flow {
        if (_cachedMovies.value.isNullOrEmpty()) {
            loadAllMovies()
        }

        emitAll(
            combine(
                _cachedMovies,
                getFavoriteIds()
            ) { movies, favoriteIds ->
                //REVIEW: toSet() serves no purpose here.
                // could also just use favoriteIds.orEmpty() or favoriteIds ?: emptySet()
                val favoritesSet = favoriteIds?.toSet() ?: emptySet()
                movies?.map { movie ->
                    movie.copy(isFavorite = movie.id in favoritesSet)
                } ?: emptyList()
            }
        )
    }

    private suspend fun loadAllMovies() {
        withContext(Dispatchers.IO) {
            _cachedMovies.value = getMoviesFromJson(MOVIES_FILE_NAME)
        }
    }

    override suspend fun getFavorites(): Flow<List<Movie>?> =
        getAllMovies().map { movies -> movies.filter { it.isFavorite } }

    override suspend fun getStaffPicks(): Flow<List<Movie>?> = flow {
        if (_cachedStaffPicks.value.isNullOrEmpty()) {
            loadStaffPicks()
        }

        emitAll(
            combine(
                _cachedStaffPicks,
                getFavoriteIds()
            ) { picks, favoriteIds ->
                val favoritesSet = favoriteIds?.toSet() ?: emptySet()
                picks?.map { pick ->
                    pick.copy(isFavorite = pick.id in favoritesSet)
                } ?: emptyList()
            }
        )
    }

    private suspend fun loadStaffPicks() {
        _cachedStaffPicks.value = withContext(Dispatchers.IO) {
            getMoviesFromJson(STAFF_PICKS_FILE_NAME)
        }
    }

    private fun getMoviesFromJson(fileName: String): List<Movie>? = try {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        val movieListType = Types.newParameterizedType(List::class.java, Movie::class.java)
        val adapter = moshi.adapter<List<Movie>>(movieListType)
        val movieList = adapter.fromJson(jsonString) ?: emptyList()
        movieList
    } catch (e: Exception) {
        Log.e(TAG, "Couldn't load movies: $e")
        null
    }

    override suspend fun getFavoriteIds(): Flow<Set<Int>?> = favoritesDataStore.data
        .map { preferences ->
            preferences[ID_LIST_KEY]?.mapFavoriteIds() ?: emptySet()
        }

    /**
     * Mapper functions that transforms a string of saved IDs to a list of IDs.
     * "1,2,3,4" -> [1,2,3,4]
     */
    private fun String.mapFavoriteIds() = this.split(COMMA).mapNotNull { it.toIntOrNull() }.toSet()

    override suspend fun toggleFavorite(id: Int) {
        getFavoriteIds().firstOrNull()?.let { ids ->
            val updatedIds = if (ids.contains(id)) {
                ids.withRemovedId(id)
            } else {
                ids.withAddedId(id)
            }
            saveFavorites(updatedIds)
        } ?: saveFavorites(setOf(id))
    }

    private fun Set<Int>.withRemovedId(id: Int) = toMutableSet().apply {
        remove(id)
    }

    private fun Set<Int>.withAddedId(id: Int) = toMutableSet().apply {
        add(id)
    }

    private suspend fun saveFavorites(favoriteIds: Set<Int>) {
        val idsString = favoriteIds.joinToString(COMMA)
        favoritesDataStore.edit { preferences ->
            preferences[ID_LIST_KEY] = idsString
        }
    }

    override suspend fun getMovieDetail(id: Int): Flow<Movie?> = flow {
        //REVIEW: could have reused getAllMovies (which already includes the favorite logic)
        // to reduce copy pasting
        if (_cachedMovies.value.isNullOrEmpty()) {
            loadAllMovies()
        }

        emitAll(
            combine(
                _cachedMovies,
                getFavoriteIds()
            ) { movies, favoriteIds ->
                val favoritesSet = favoriteIds?.toSet() ?: emptySet()
                movies?.firstOrNull { it.id == id }?.let { movie ->
                    movie.copy(isFavorite = movie.id in favoritesSet)
                }
            }
        )
    }

    companion object {
        private const val TAG = "MovieRepositoryImpl"
        private val ID_LIST_KEY = stringPreferencesKey("favorites_id_list")
        private const val MOVIES_FILE_NAME = "movies.json"
        private const val STAFF_PICKS_FILE_NAME = "staff_picks.json"
        private const val COMMA = ","
    }
}
