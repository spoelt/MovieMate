package com.spoelt.moviemate.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spoelt.moviemate.domain.model.Movie
import com.spoelt.moviemate.presentation.components.MoviePoster
import com.spoelt.moviemate.presentation.components.MoviePosterType

@Composable
fun FavoritesList(
    modifier: Modifier = Modifier,
    favorites: List<Movie>,
    onClick: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        items(favorites) { favorite ->
            Card(
                modifier = Modifier.clickable { onClick(favorite.id) },
                shape = RoundedCornerShape(20.dp),
                elevation = 8.dp
            ) {
                MoviePoster(
                    url = favorite.posterUrl,
                    title = favorite.title,
                    type = MoviePosterType.FAVORITES
                )
            }
        }
    }
}