package com.spoelt.moviemate.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie(
    val id: Int,
    val rating: Float?,
    val revenue: Int?,
    val releaseDate: String?,
    val posterUrl: String?,
    val runtime: Int?,
    val title: String?,
    val overview: String?,
    val budget: Int?,
    val genres: List<String>?,
    val isFavorite: Boolean = false,
    val reviews: Int?,
    val language: String?
)
