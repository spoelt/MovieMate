package com.spoelt.moviemate.presentation.allmovies.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spoelt.moviemate.domain.model.Movie
import com.spoelt.moviemate.presentation.components.MovieItem
import com.spoelt.moviemate.presentation.components.MoviePosterType
import com.spoelt.moviemate.presentation.theme.MovieMateTheme

@Composable
fun MoviesList(
    modifier: Modifier = Modifier,
    movies: List<Movie>,
    onToggleFavorite: (Int) -> Unit,
    onClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        itemsIndexed(movies) { index, movie ->
            if (index == 0) Spacer(modifier = Modifier.height(35.dp))

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
        }
    }
}

//REVIEW: showBackground = true would improve the preview
@Preview
@Composable
private fun MoviesListPreview() {
    MovieMateTheme {
        MoviesList(
            movies = (1..6).map {
                Movie(
                    id = it,
                    rating = 0.5f * it,
                    releaseDate = "2024-09-30",
                    revenue = null,
                    posterUrl = null,
                    runtime = null,
                    title = "Movie $it",
                    overview = null,
                    budget = null,
                    genres = null,
                    isFavorite = it % 2 == 0,
                    reviews = null,
                    language = null,
                )
            },
            onToggleFavorite = {},
            onClick = {}
        )
    }
}
