package com.spoelt.moviemate.presentation.moviedetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spoelt.moviemate.R
import com.spoelt.moviemate.presentation.theme.MovieMateTheme
import com.spoelt.moviemate.presentation.theme.withAlpha

@Composable
fun MovieFacts(
    modifier: Modifier = Modifier,
    facts: List<Pair<String, String>>
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(bottom = 12.dp),
            text = stringResource(R.string.key_facts),
            style = MaterialTheme.typography.body1
        )

        for (i in facts.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FactCard(
                    modifier = Modifier.weight(1f),
                    title = facts[i].first,
                    value = facts[i].second,
                )

                Spacer(modifier = Modifier.width(12.dp))

                if (i + 1 < facts.size) {
                    FactCard(
                        modifier = Modifier.weight(1f),
                        title = facts[i + 1].first,
                        value = facts[i + 1].second,
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            if (i + 2 < facts.size) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun FactCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.LightGray.withAlpha(0.4f),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body2,
                color = colorResource(id = R.color.middle_gray),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                modifier = Modifier.padding(top = 3.dp),
                text = value,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun MovieFactsPreview() {
    MovieMateTheme {
        MovieFacts(
            facts = listOf(
                "Budget" to "$1.000.000",
                "Revenue" to "$14.000.000",
                "Original language" to "English",
                "Rating" to "3.82 (1734)"
            )
        )
    }
}