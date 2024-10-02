package com.spoelt.moviemate.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spoelt.moviemate.domain.model.Movie
import com.spoelt.moviemate.presentation.components.MovieItem
import com.spoelt.moviemate.presentation.components.MoviePosterType

@Composable
fun StaffPicks(
    picks: List<Movie>,
    onToggleFavorite: (Int) -> Unit,
    onClick: (Int) -> Unit
) {
    Column {
        picks.forEachIndexed { index, movie ->
            if (index == 0) Spacer(modifier = Modifier.height(24.dp))

            MovieItem(
                title = movie.title,
                releaseDate = movie.releaseDate,
                posterUrl = movie.posterUrl,
                rating = movie.rating,
                isFavorite = movie.isFavorite,
                onToggleFavorite = {
                    onToggleFavorite(movie.id)
                },
                type = MoviePosterType.MOVIE_LIST,
                onClick = {
                    onClick(movie.id)
                }
            )

            Spacer(modifier = Modifier.height(if (index != picks.lastIndex) 35.dp else 24.dp))
        }
    }
}