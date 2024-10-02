package com.spoelt.moviemate.presentation.home

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.spoelt.moviemate.R
import com.spoelt.moviemate.domain.model.Movie
import com.spoelt.moviemate.presentation.home.components.FavoritesList
import com.spoelt.moviemate.presentation.home.components.StaffPicks
import com.spoelt.moviemate.presentation.theme.MovieMateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val staffPicksTitle = view.findViewById<TextView>(R.id.staff_picks_title)
        val staffPicksList = view.findViewById<ComposeView>(R.id.staff_picks_compose_view)
        val favoritesTitle = view.findViewById<TextView>(R.id.favorites_title)
        val favoritesList = view.findViewById<ComposeView>(R.id.favorites_compose_view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                view.findViewById<ProgressBar>(R.id.progress_bar_home).visibility =
                    if (isLoading) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiData.collect { data ->
                favoritesTitle.visibility =
                    if (data.favorites.isNullOrEmpty()) View.GONE else View.VISIBLE
                favoritesList.visibility = favoritesTitle.visibility

                staffPicksTitle.visibility =
                    if (data.staffPicks.isNullOrEmpty()) View.GONE else View.VISIBLE
                staffPicksList.visibility = staffPicksTitle.visibility

                view.findViewById<ConstraintLayout>(R.id.header).visibility =
                    if (data.userName == null) View.GONE else View.VISIBLE

                data.userName?.let { name ->
                    val initialText = getString(R.string.welcome_string, name)
                    view.findViewById<TextView>(R.id.welcome_message).text = getSpannableString(
                        initialString = initialText,
                        index = initialText.indexOfFirst { it == LINE_BREAK }.plus(1),
                    )
                }

                staffPicksTitle.text = getSpannableString(
                    initialString = getString(R.string.staff_picks_title).uppercase(),
                    index = getString(R.string.staff_picks_title)
                        .indexOfFirst { it == SPACE }
                        .plus(1),
                )

                favoritesTitle.text = getSpannableString(
                    initialString = getString(R.string.your_favorites_title).uppercase(),
                    index = getString(R.string.your_favorites_title)
                        .indexOfFirst { it == SPACE }
                        .plus(1),
                )

                data.favorites?.let { favs ->
                    favoritesList.setupFavorites(
                        favorites = favs,
                        navController = navController
                    )
                }

                data.staffPicks?.let { picks ->
                    staffPicksList.setupStaffPicks(
                        picks = picks,
                        navController = navController
                    )
                }
            }
        }

        view.findViewById<CardView>(R.id.search_button).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_allMoviesFragment)
        }
    }

    private fun ComposeView.setupFavorites(
        favorites: List<Movie>,
        navController: NavController
    ) = apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            MovieMateTheme {
                FavoritesList(
                    favorites = favorites,
                    onClick = { id ->
                        val bundle = bundleOf(MOVIE_ID_KEY to id)
                        navController.navigate(
                            R.id.action_homeFragment_to_movieDetailFragment,
                            bundle
                        )
                    }
                )
            }
        }
    }

    private fun ComposeView.setupStaffPicks(
        picks: List<Movie>,
        navController: NavController
    ) = apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            MovieMateTheme {
                StaffPicks(
                    picks = picks,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onClick = { id ->
                        val bundle = bundleOf(MOVIE_ID_KEY to id)
                        navController.navigate(
                            R.id.action_homeFragment_to_movieDetailFragment,
                            bundle
                        )
                    }
                )
            }
        }
    }

    /**
     * Returns a spannable, styled string.
     *
     * @param initialString The string that should be turned into a Spannable
     * @param index The index from which on the formatting should be different
     */
    private fun getSpannableString(
        initialString: String,
        index: Int,
    ): SpannableString {
        return SpannableString(initialString).apply {
            setSpan(
                TextAppearanceSpan(requireContext(), R.style.SubheaderNormal),
                0,
                index,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                TextAppearanceSpan(requireContext(), R.style.SubheaderBold),
                index,
                initialString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    companion object {
        private const val SPACE = ' '
        private const val LINE_BREAK = '\n'
        private const val MOVIE_ID_KEY = "movie_id"
    }
}