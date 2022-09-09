package com.donc.tmdbapp.movielist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.donc.tmdbapp.R
import com.donc.tmdbapp.models.MovieListEntry
import com.donc.tmdbapp.util.Constants.IMAGE_URL

//TODO: Revisar Estilos en general. Agregar animaciones
@Composable
fun MovieListScreen(
    navController: NavController,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.ic_tmdb),
                contentDescription = stringResource(R.string.desc_logo),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
                    .padding(24.dp)
            )
            SearchBar {
                viewModel.searchMoviesList(it)
            }
            Spacer(modifier = Modifier.height(18.dp))
            MoviesList(navController = navController)
        }
    }
}

@Composable
fun MoviesList(
    navController: NavController,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val moviesList by remember { viewModel.moviesList }
    val endReached by remember { viewModel.endReached }
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadError }
    val isSearching by remember { viewModel.isSearching }

    LazyColumn(contentPadding = PaddingValues(12.dp)) {
        val itemCount = if (moviesList.size % 2 == 0) {
            moviesList.size / 2
        } else {
            moviesList.size / 2 + 1
        }
        items(itemCount) {
            if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                viewModel.loadMoviesPaginated()
            }
            MovieRow(rowIndex = it, moviesList, navController)
        }
    }
    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadMoviesPaginated()
            }
        }
    }
}

@Composable
fun MovieRow(
    rowIndex: Int,
    entries: List<MovieListEntry>,
    navController: NavController
) {
    Column {
        Row {
            MovieEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                MovieEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun MovieEntry(
    entry: MovieListEntry,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { navController.navigate(route = "movie_details/${entry.movie_id}") }
            .border(
                width = 3.dp,
                brush = Brush.verticalGradient(
                    listOf(MaterialTheme.colorScheme.tertiary, Color.Transparent),
                    0f
                ), shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
        ,
        shape = RoundedCornerShape(10.dp),
    ) {
        Box(modifier = Modifier.height(200.dp)){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(IMAGE_URL + entry.poster_path)
                    .placeholder(CircularProgressDrawable(LocalContext.current))
                    .crossfade(true)
                    .build(),
                contentDescription = entry.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black),
                        200f
                    )
                ),
                contentAlignment = BottomCenter
            ){
                Text(
                    text = entry.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            ),
            decorationBox = { innerTextField ->
                /* Agregado solo porque el BasicTextField no posee icono, y el TextField clasico
                tiene un diseño por defecto que no me gusta para una barra de busqueda */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(5.dp, MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(R.string.desc_search),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    ) {
                        if (text.isEmpty()) Text(
                            stringResource(R.string.search_hint),
                            style = LocalTextStyle.current.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                fontStyle = FontStyle.Italic
                            )
                        )
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = MaterialTheme.colorScheme.error, fontSize = 24.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.button_retry))
        }
    }
}