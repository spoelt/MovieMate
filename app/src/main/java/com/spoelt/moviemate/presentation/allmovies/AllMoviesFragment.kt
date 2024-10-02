package com.spoelt.moviemate.presentation.allmovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.spoelt.moviemate.R
import com.spoelt.moviemate.presentation.allmovies.components.MoviesList
import com.spoelt.moviemate.presentation.allmovies.components.SearchBar
import com.spoelt.moviemate.presentation.theme.MovieMateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AllMoviesFragment : Fragment() {
    private val viewModel: AllMoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val progressBar = view.findViewById<View>(R.id.progress_bar)
        val moviesList = view.findViewById<ComposeView>(R.id.all_movies_list)
        val searchBar = view.findViewById<ComposeView>(R.id.all_movies_search_bar)

        viewModel.isLoading.onEach { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            moviesList.visibility = if (isLoading) View.GONE else View.VISIBLE
            searchBar.visibility = if (isLoading) View.GONE else View.VISIBLE
        }.launchIn(lifecycleScope)

        moviesList.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MovieMateTheme {
                    val movies by viewModel.filteredMovies.collectAsStateWithLifecycle(
                        initialValue = null
                    )

                    movies?.let { movs ->
                        MoviesList(
                            modifier = Modifier,
                            movies = movs,
                            onToggleFavorite = viewModel::toggleFavorite,
                            onClick = { id ->
                                val bundle = bundleOf(MOVIE_ID_KEY to id)
                                navController.navigate(
                                    R.id.action_allMoviesFragment_to_movieDetailFragment,
                                    bundle
                                )
                            }
                        )
                    }
                }
            }
        }

        searchBar.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MovieMateTheme {
                    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

                    SearchBar(
                        searchQuery = searchQuery,
                        onSearch = viewModel::search
                    )
                }
            }
        }

        view.findViewById<View>(R.id.all_movies_back_button).setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val MOVIE_ID_KEY = "movie_id"
    }
}