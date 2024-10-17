package com.spoelt.moviemate.presentation.allmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.moviemate.domain.model.Movie
import com.spoelt.moviemate.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    //REVIEW: instead of splitting loading & data it would be preferable
    // to have a wrapper class which can either be Loading or Loaded
    // that way it would also be easier to add error states later
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _allMovies = MutableStateFlow<List<Movie>?>(null)
    private val allMovies = _allMovies.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredMovies = combine(
        allMovies,
        searchQuery
    ) { allMovies, query ->
        if (query.isBlank()) {
            allMovies
        } else {
            allMovies?.filter { movie ->
                movie.title?.contains(query, ignoreCase = true) == true
            }
        }
    }

    init {
        getMovies()
    }

    private fun getMovies() {
        viewModelScope.launch {
            movieRepository.getAllMovies().collect { movies ->
                _allMovies.value = movies
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(id: Int) {
        viewModelScope.launch {
            movieRepository.toggleFavorite(id)
        }
    }

    fun search(query: String) {
        _searchQuery.value = query
    }
}
