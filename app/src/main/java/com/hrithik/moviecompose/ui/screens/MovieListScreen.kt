package com.hrithik.moviecompose.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.hrithik.moviecompose.viewModel.MovieListViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun MovieCardList(movieListViewModel: MovieListViewModel = koinViewModel()) {
    val movieDetails by movieListViewModel.movieListUIState.collectAsState()
    val scrollState = rememberLazyGridState()
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.lastOrNull()?.index == movieDetails.movies.lastIndex) {
                    movieListViewModel.loadNextPage()
                }
            }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (movieDetails.movieUIState) {
            MovieListViewModel.MovieListViewModelState.IN_PROGRESS -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                )
            }

            MovieListViewModel.MovieListViewModelState.ERROR_MOVIE_LIST -> {
                Text(
                    text = "Failed to load movies. Please try again.",
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movieDetails.movies.size) { index ->
                        val movie = movieDetails.movies[index]
                        MovieCard(
                            imageUrl = movie.imageUrl,
                            likes = movie.popularity,
                            title = movie.movieTitle,
                            releaseDate = movie.releaseDate
                        )
                    }
                    if (movieDetails.isLoadingNextPage) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(alignment = Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerCircularProgressBar(
    progress: Int = 0,
    startAngle: Float = 270f,
    size: Dp = 40.dp,
    strokeWidth: Dp = 12.dp,
    backgroundArcColor: Color = Color.LightGray,
    progressArcColor1: Color = Color.Blue,
    progressArcColor2: Color = progressArcColor1,
    modifier: Modifier = Modifier
) {
    val staticProgress = progress / 100f

    Canvas(
        modifier = modifier
            .size(size)
    ) {
        // Progress Animation Implementation
        val strokeWidthPx = strokeWidth.toPx()
        val arcSize = size.toPx() - strokeWidthPx
        // Gradient Brush
        val gradientBrush = Brush.verticalGradient(
            colors = listOf(progressArcColor1, progressArcColor2, progressArcColor1)
        )
        drawCircle(
            color = Color.Black,
            radius = arcSize / 2,
            center = center
        )
        // Progress Arc Implementation
        withTransform({
            rotate(degrees = startAngle, pivot = center)
        }) {
            drawArc(
                brush = gradientBrush,
                startAngle = 0f,
                sweepAngle = staticProgress * 360,
                useCenter = false,
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                size = Size(arcSize, arcSize),
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
        }
    }
}


@Preview
@Composable
fun CustomerCircularProgressBarPreview() {
    CustomerCircularProgressBar(
        progress = 85,
        progressArcColor1 = Color(0xFF673AB7),
        progressArcColor2 = Color(0xFF4CAF50),
    )
}

@Preview
@Composable
fun MovieListPreview() {
    MovieCard("https://images.app.goo.gl/YBS3XeHqztnAgEkcA", 100, "Terrifier 3", "Oct 09, 2024")
}