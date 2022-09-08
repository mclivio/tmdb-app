package com.donc.tmdbapp.movielist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.donc.tmdbapp.R

//TODO: Revisar Estilos en general. Agregar animaciones
@Composable
fun MovieListScreen(
    navController: NavController
    //TODO: Implementar un viewModel
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
                    .align(Alignment.CenterHorizontally)
                    .padding(24.dp)
            )
            SearchBar() {
                //TODO: Implementar busqueda en viewModel
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
                            .padding(horizontal = 12.dp)) {
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

//region PREVIEWS
@Preview
@Composable
fun prevSearchBar() {
    SearchBar()
}
//endregion