package com.spoelt.moviemate.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.moviemate.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _uiData = MutableStateFlow(HomeUiState())
    val uiData = _uiData.asStateFlow()

    init {
        getHomeData()
    }

    private fun getHomeData() {
        viewModelScope.launch {
            combine(
                movieRepository.getFavorites(),
                movieRepository.getStaffPicks(),
            ) { favorites, staffPicks ->
                HomeUiState(
                    favorites = favorites,
                    staffPicks = staffPicks,
                    userName = "Jane Doe"
                )
            }.collect { data ->
                _isLoading.value = false
                _uiData.value = data
            }
        }
    }

    fun toggleFavorite(id: Int) {
        viewModelScope.launch {
            movieRepository.toggleFavorite(id)
        }
    }
}
