package com.spoelt.moviemate.presentation.home

import com.spoelt.moviemate.domain.model.Movie

data class HomeUiState(
    val favorites: List<Movie>? = null,
    val staffPicks: List<Movie>? = null,
    val userName: String? = null
)