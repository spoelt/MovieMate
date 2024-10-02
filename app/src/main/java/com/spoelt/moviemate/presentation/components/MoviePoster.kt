package com.spoelt.moviemate.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.spoelt.moviemate.R
import com.spoelt.moviemate.presentation.theme.MovieMateTheme

enum class MoviePosterType {
    DETAIL, FAVORITES, MOVIE_LIST
}

@Composable
fun MoviePoster(
    url: String?,
    title: String?,
    type: MoviePosterType
) {
    val isPreview = LocalInspectionMode.current
    val imageSize = remember {
        type.getImageSizeForType()
    }

    if (isPreview || url == null) {
        Image(
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(14.dp)),
            painter = painterResource(id = R.drawable.placeholder_image),
            contentDescription = title,
            contentScale = ContentScale.Inside,
        )
    } else {
        AsyncImage(
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(14.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            placeholder = painterResource(R.drawable.placeholder_image),
            fallback = painterResource(R.drawable.placeholder_image),
            contentDescription = title,
            contentScale = ContentScale.FillBounds,
        )
    }
}

private fun MoviePosterType.getImageSizeForType() = when (this) {
    MoviePosterType.DETAIL -> DpSize(
        width = 203.dp,
        height = 296.dp
    )

    MoviePosterType.FAVORITES -> DpSize(
        width = 182.dp,
        height = 270.dp
    )

    MoviePosterType.MOVIE_LIST -> DpSize(
        width = 60.dp,
        height = 90.dp
    )
}

@Preview
@Composable
private fun MoviePosterPreview() {
    MovieMateTheme {
        MoviePoster(
            url = "",
            type = MoviePosterType.DETAIL,
            title = "Toy Story"
        )
    }
}