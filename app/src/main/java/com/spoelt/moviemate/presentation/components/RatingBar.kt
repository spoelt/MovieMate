package com.spoelt.moviemate.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spoelt.moviemate.R
import com.spoelt.moviemate.presentation.theme.MovieMateTheme
import kotlin.math.floor

val RATING_RANGE = 1..5

@Composable
fun RatingBar(
    rating: Float,
    modifier: Modifier = Modifier,
    starSize: Dp = 12.dp
) {
    val formattedRating = remember(rating) {
        floor(rating).toInt()
    }

    Row(
        modifier = modifier,
    ) {
        for (i in RATING_RANGE) {
            StarIcon(
                isFilled = i <= formattedRating,
                starSize = starSize
            )
        }
    }
}

@Composable
private fun StarIcon(
    isFilled: Boolean,
    starSize: Dp
) {
    val contentDescId = remember(isFilled) {
        if (isFilled) R.string.filled_star_icon_content_desc else R.string.unfilled_star_icon_content_desc
    }
    val imageResId = remember(isFilled) {
        if (isFilled) R.drawable.star_filled else R.drawable.star_blank
    }

    Image(
        modifier = Modifier.size(starSize),
        painter = painterResource(id = imageResId),
        contentDescription = stringResource(id = contentDescId),
    )
}

@Preview
@Composable
fun RatingBarPreview() {
    MovieMateTheme {
        RatingBar(
            rating = 3.4f,
            starSize = 14.dp
        )
    }
}