package com.spoelt.moviemate.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spoelt.moviemate.R
import com.spoelt.moviemate.presentation.theme.MovieMateTheme
import com.spoelt.moviemate.presentation.theme.withAlpha
import com.spoelt.moviemate.presentation.util.DateTimeFormatter.getYearFromDateString

@Composable
fun MovieItem(
    title: String?,
    releaseDate: String?,
    posterUrl: String?,
    rating: Float?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    type: MoviePosterType,
    onClick: () -> Unit
) {
    val year = remember(releaseDate) {
        getYearFromDateString(releaseDate)
    }
    val iconResId = remember(isFavorite) {
        if (isFavorite) R.drawable.bookmarked else R.drawable.bookmark
    }
    val iconContentDesc = remember(isFavorite) {
        if (isFavorite) R.string.bookmarked else R.string.to_bookmark
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MoviePoster(
            url = posterUrl,
            title = title,
            type = type
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            year?.let { y ->
                Text(
                    text = y,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.withAlpha(0.6f)
                )
            }

            title?.let { t ->
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = t,
                    style = MaterialTheme.typography.body1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            rating?.let { r ->
                RatingBar(
                    modifier = Modifier.padding(top = 4.dp),
                    rating = r,
                )
            }
        }

        IconButton(onClick = onToggleFavorite) {
            Image(
                modifier = Modifier.size(
                    width = 16.dp,
                    height = 24.dp
                ),
                painter = painterResource(id = iconResId),
                contentDescription = stringResource(id = iconContentDesc)
            )
        }
    }
}

@Preview
@Composable
private fun MovieItemPreview() {
    MovieMateTheme {
        MovieItem(
            title = "Movie",
            releaseDate = "2024-09-30",
            posterUrl = null,
            rating = 3.8f,
            isFavorite = true,
            onToggleFavorite = {},
            type = MoviePosterType.MOVIE_LIST,
            onClick = {}
        )
    }
}