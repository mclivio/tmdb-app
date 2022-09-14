package com.donc.tmdbapp.moviedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceEvenly
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.donc.tmdbapp.R
import com.donc.tmdbapp.data.remote.responses.Movie
import com.donc.tmdbapp.util.Constants
import com.donc.tmdbapp.util.Resource
import java.util.*


@Composable
fun MovieDetailsScreen(
    movie_id: Int,
    navController: NavController,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val movieInfo = produceState<Resource<Movie>>(initialValue = Resource.Loading()) {
        value = viewModel.getMovieInfo(movie_id)
    }.value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (movieInfo is Resource.Success) {
                movieInfo.data?.poster_path?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(Constants.IMAGE_URL + movieInfo.data.poster_path)
                            .placeholder(CircularProgressDrawable(LocalContext.current))
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(R.string.desc_poster) + movieInfo.data.title,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
            IconButton(
                modifier = Modifier
                    .padding(12.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        shape = CircleShape
                    ),
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.button_back),
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        }
        //info section
        Box(
            contentAlignment = Center,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp, 10.dp))
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.95f),
                            MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.85f)
                        )
                    ),
                    shape = RoundedCornerShape(26.dp, 26.dp)
                )
                .border(
                    width = 3.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0x3BFFFFFF),
                            Color(0x00FFFFFF),
                            Color.Transparent
                        ),
                        0f
                    ), shape = RoundedCornerShape(26.dp, 26.dp)
                )
        ) {
            MovieDetailsStateWrapper(movieInfo = movieInfo)
        }
    }
}

@Composable
fun MovieDetailsStateWrapper(
    movieInfo: Resource<Movie>,
) {
    when (movieInfo) {
        is Resource.Success -> {
            MovieDetailsInfoSection(
                movieInfo = movieInfo.data!!,
                modifier = Modifier.fillMaxSize()
            )
        }
        is Resource.Error -> {
            Text(
                text = movieInfo.message!!,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun MovieDetailsInfoSection(
    movieInfo: Movie,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = movieInfo.title,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, 4.dp, 4.dp, 0.dp)
            )
            Row(
                horizontalArrangement = SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                movieInfo.genres.forEach {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .shadow(2.dp, shape = CircleShape)
                            .height(22.dp)
                            .padding(6.dp, 0.dp),
                        contentAlignment = Center
                    ) {
                        Text(
                            text = it.name.uppercase(Locale.ROOT),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Text(
                buildAnnotatedString {
                    append(
                        AnnotatedString(
                            text = stringResource(R.string.label_language),
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        )
                    )
                    append(" " + movieInfo.original_language.uppercase(Locale.ROOT))
                },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.padding(10.dp, 1.dp, 10.dp, 1.dp)
            )
            Text(
                buildAnnotatedString {
                    append(
                        AnnotatedString(
                            text = stringResource(R.string.label_date),
                            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
                        )
                    )
                    append(" " + movieInfo.release_date.uppercase(Locale.ROOT))
                },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier.padding(10.dp, 1.dp, 10.dp, 1.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Start)
                    .padding(10.dp, 2.dp, 10.dp, 2.dp),
                verticalAlignment = CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = stringResource(R.string.desc_popularity),
                    tint = MaterialTheme.colorScheme.inversePrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${String.format("%.2f", movieInfo.vote_average)}/10",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
            Text(
                text = movieInfo.overview,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 1.dp, 10.dp, 1.dp)
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}