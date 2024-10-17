package com.spoelt.moviemate.presentation.moviedetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spoelt.moviemate.presentation.theme.MovieMateTheme
import com.spoelt.moviemate.presentation.theme.withAlpha

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Genres(
    modifier: Modifier = Modifier,
    genres: List<String>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            genres.forEach { genre ->
                Card(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    backgroundColor = Color.LightGray.withAlpha(0.4f),
                    elevation = 0.dp,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        //REVIEW: horizontal padding should be 8.dp
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        text = genre,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GenresPreview() {
    MovieMateTheme {
        Genres(
            modifier = Modifier.width(200.dp),
            genres = listOf("Action", "SciFi", "Drama", "Thriller")
        )
    }
}