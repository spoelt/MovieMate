package com.spoelt.moviemate.presentation.moviedetail

import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.spoelt.moviemate.R
import com.spoelt.moviemate.presentation.components.MoviePoster
import com.spoelt.moviemate.presentation.components.MoviePosterType
import com.spoelt.moviemate.presentation.components.RatingBar
import com.spoelt.moviemate.presentation.moviedetail.components.Genres
import com.spoelt.moviemate.presentation.moviedetail.components.MovieFacts
import com.spoelt.moviemate.presentation.theme.MovieMateTheme
import com.spoelt.moviemate.presentation.util.DateTimeFormatter
import com.spoelt.moviemate.presentation.util.StringFormatter.mapBudget
import com.spoelt.moviemate.presentation.util.StringFormatter.mapLanguage
import com.spoelt.moviemate.presentation.util.StringFormatter.mapRating
import com.spoelt.moviemate.presentation.util.StringFormatter.mapRevenue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.getInt(MOVIE_ID_KEY)?.let { id ->
            viewModel.getMovie(id)
        }

        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                view.apply {
                    findViewById<ProgressBar>(R.id.progress_bar_detail).visibility =
                        if (isLoading) View.VISIBLE else View.GONE

                    findViewById<ScrollView>(R.id.detail_content).visibility =
                        if (isLoading) View.GONE else View.VISIBLE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movie.collect { movie ->
                view.apply {
                    findViewById<ComposeView>(R.id.movie_detail_poster).setupMoviePoster(
                        url = movie?.posterUrl,
                        title = movie?.title
                    )

                    findViewById<ImageButton>(R.id.bookmark_button).setupBookmarkButton(
                        isFavorite = movie?.isFavorite ?: false,
                        id = movie?.id
                    )

                    movie?.rating?.let { rating ->
                        findViewById<ComposeView>(R.id.rating_bar).setupRatingBar(rating)
                    }

                    findViewById<TextView>(R.id.release_date_and_length)
                        .setupReleaseDateAndLength(
                            release = movie?.releaseDate,
                            time = movie?.runtime
                        )

                    findViewById<TextView>(R.id.title).setupTitle(
                        release = movie?.releaseDate,
                        title = movie?.title
                    )

                    findViewById<ComposeView>(R.id.genres).setupGenres(movie?.genres)

                    findViewById<TextView>(R.id.overview_header).apply {
                        visibility = if (movie?.overview != null) View.VISIBLE else View.GONE
                    }

                    findViewById<TextView>(R.id.overview_text).apply {
                        movie?.overview?.let { overview -> text = overview }
                        visibility = if (movie?.overview != null) View.VISIBLE else View.GONE
                    }

                    findViewById<ComposeView>(R.id.facts).apply {
                        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                        val facts = getMappedMovieFacts(
                            budget = movie?.budget,
                            revenue = movie?.revenue,
                            language = movie?.language,
                            rating = movie?.rating,
                            reviews = movie?.reviews
                        )
                        visibility = if (facts.isNotEmpty()) View.VISIBLE else View.GONE
                        setContent {
                            MovieMateTheme {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 30.dp),
                                ) {
                                    MovieFacts(facts = facts)
                                }
                            }
                        }
                    }
                }
            }
        }

        view.findViewById<ImageButton>(R.id.close_button).setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun ImageButton.setupBookmarkButton(isFavorite: Boolean, id: Int?) = this.apply {
        setImageResource(
            if (isFavorite) R.drawable.bookmarked else R.drawable.bookmark
        )
        setOnClickListener {
            id?.let { viewModel.toggleFavorite(it) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun TextView.setupReleaseDateAndLength(
        release: String?,
        time: Int?
    ) {
        val releaseDate = release?.let {
            DateTimeFormatter.formatDate(date = it)
        }
        val runtime = time?.let { DateTimeFormatter.formatRuntime(it) }
        if (releaseDate != null && runtime != null) {
            this.apply {
                text =
                    getString(R.string.release_data_and_length, releaseDate, runtime)
                setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.middle_gray
                    )
                )
            }
            visibility = View.VISIBLE
        } else {
            visibility = View.GONE
        }
    }

    private fun TextView.setupTitle(
        release: String?,
        title: String?
    ) {
        val year = DateTimeFormatter.getYearFromDateString(release)
        if (year != null && title != null) {
            val titleAndYear = getString(R.string.movie_title_year, title, year)
            text = getSpannableString(
                initialString = titleAndYear,
                index = titleAndYear.indexOfFirst { it == PARENTHESIS }
            )
            visibility = View.VISIBLE
        } else {
            visibility = View.GONE
        }
    }

    private fun ComposeView.setupRatingBar(rating: Float) = this.apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            MovieMateTheme {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RatingBar(
                        rating = rating,
                        starSize = 18.dp
                    )
                }
            }
        }
    }

    private fun ComposeView.setupMoviePoster(
        url: String?,
        title: String?
    ) = this.apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            MovieMateTheme {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MoviePoster(
                        url = url,
                        title = title,
                        type = MoviePosterType.DETAIL
                    )
                }
            }
        }
    }

    private fun ComposeView.setupGenres(genres: List<String>?) = this.apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            genres?.let { g ->
                MovieMateTheme {
                    Genres(
                        modifier = Modifier.fillMaxWidth(),
                        genres = g
                    )
                }
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
                TextAppearanceSpan(requireContext(), R.style.HeaderBold),
                0,
                index,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.black)),
                0,
                index,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                TextAppearanceSpan(requireContext(), R.style.HeaderNormal),
                index,
                initialString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.middle_gray)),
                index,
                initialString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun getMappedMovieFacts(
        budget: Int?,
        revenue: Int?,
        language: String?,
        rating: Float?,
        reviews: Int?
    ): List<Pair<String, String>> {
        return listOfNotNull(
            mapBudget(budget)?.let { getString(R.string.budget_title) to it },
            mapRevenue(revenue)?.let { getString(R.string.revenue_title) to it },
            mapLanguage(language)?.let { getString(R.string.language_title) to it },
            mapRating(rating, reviews)?.let { getString(R.string.rating_title) to it }
        )
    }

    companion object {
        private const val PARENTHESIS = '('
        private const val MOVIE_ID_KEY = "movie_id"
    }
}