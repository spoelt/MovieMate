package com.spoelt.moviemate.presentation.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.moviemate.domain.model.Movie
import com.spoelt.moviemate.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie = _movie.asStateFlow()

    fun getMovie(id: Int) {
        viewModelScope.launch {
            movieRepository.getMovieDetail(id).collect { movie ->
                _movie.value = movie
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(id: Int) {
        viewModelScope.launch {
            movieRepository.toggleFavorite(id)
        }
    }
}