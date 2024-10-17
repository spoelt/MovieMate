package com.spoelt.moviemate.presentation.allmovies.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spoelt.moviemate.R
import com.spoelt.moviemate.presentation.theme.MovieMateTheme
import com.spoelt.moviemate.presentation.theme.withAlpha

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearch: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = searchQuery,
        onValueChange = {
            onSearch(it)
        },
        placeholder = {
            //REVIEW: design uses a fontSize of 14.sp
            Text(
                text = stringResource(R.string.search_action),
                color = Color.Gray
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
        ),
        singleLine = true,
        leadingIcon = {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_action),
                tint = MaterialTheme.colors.onSurface.withAlpha(0.4f)
            )
        },
        //REVIEW: design uses 16dp
        shape = RoundedCornerShape(12.dp)
    )
}

@Preview
@Composable
private fun SearchBarEmptyPreview() {
    MovieMateTheme {
        SearchBar(
            searchQuery = "",
            onSearch = {}
        )
    }
}

@Preview
@Composable
private fun SearchBarFilledPreview() {
    MovieMateTheme {
        SearchBar(
            searchQuery = "Toy Story",
            onSearch = {}
        )
    }
}
