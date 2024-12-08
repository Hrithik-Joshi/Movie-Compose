package com.hrithik.moviecompose.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.hrithik.moviecompose.data.model.Movie
import com.hrithik.moviecompose.viewModel.MovieListViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun MovieCardList(movieListViewModel: MovieListViewModel = koinViewModel()) {
    val movieDetails by movieListViewModel.movieListUIState.collectAsState()
    var selectedMovie by remember { mutableStateOf<MovieListViewModel.MovieData?>(null) }
    var isDialogVisible by remember { mutableStateOf(false) }
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
                            releaseDate = movie.releaseDate,
                            onClick = {
                                selectedMovie = movie
                                isDialogVisible = true
                            }
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
        if (isDialogVisible && selectedMovie != null) {
            MovieDetailsDialog(
                movie = selectedMovie!!,
                onDismiss = { isDialogVisible = false },
                movieDetails = movieDetails,
                viewModel = movieListViewModel
            )
        }
    }
}

@Composable
fun CustomerCircularProgressBar(
    progress: Int = 0,
    startAngle: Float = 270f,
    size: Dp = 40.dp,
    strokeWidth: Dp = 12.dp,
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

@Composable
fun MovieDetailsDialog(
    movie: MovieListViewModel.MovieData, onDismiss: () -> Unit,
    movieDetails: MovieListViewModel.MovieUIState,
    viewModel: MovieListViewModel
) {
    var isFavorite by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {},
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = movie.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                movie.movieTitle?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .padding(top = 8.dp)
                    )
                }
                Text(
                    text = "Release Date: ${movie.releaseDate}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                movie.overview?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = when {
                            !isFavorite -> "Add To Favorite"
                            isFavorite && movieDetails.movieUIState == MovieListViewModel.MovieListViewModelState.SUCCESS_DB -> "Remove From Favorite"
                            else -> "Processing..."
                        },
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(5.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = when {
                            isFavorite && movieDetails.movieUIState == MovieListViewModel.MovieListViewModelState.SUCCESS_DB -> Color.Red
                            !isFavorite -> Color.Gray
                            else -> Color.Gray
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    isFavorite = !isFavorite
                                    if (isFavorite) {
                                        viewModel.insertMovieToDB(
                                            Movie(
                                                id = movie.movie_id,
                                                title = movie.movieTitle,
                                                release_date = movie.releaseDate,
                                                overview = movie.overview,
                                                poster_path = movie.imageUrl,
                                                vote_average = movie.popularity?.toDouble()
                                            )
                                        )
                                    } else {
                                        movie.movie_id?.let {
                                            viewModel.deleteMovieFromDB(it)
                                        }
                                    }
                                })
                            }
                    )
                }
            }
        }
    )
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
