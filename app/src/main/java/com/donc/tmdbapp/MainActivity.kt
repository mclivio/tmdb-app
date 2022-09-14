package com.donc.tmdbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.donc.tmdbapp.moviedetails.MovieDetailsScreen
import com.donc.tmdbapp.movielist.MovieListScreen
import com.donc.tmdbapp.ui.theme.TmdbAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TmdbAppTheme {
                /* NAVEGACION
                DOC: https://developer.android.com/jetpack/compose/navigation?hl=es-419 */
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "movies_list")
                {
                    composable("movies_list") {
                        MovieListScreen(navController = navController)
                    }
                    composable(
                        "movie_details/{movie_id}",
                        arguments = listOf(navArgument("movie_id") { type = NavType.IntType })
                    ) {
                        val movie_id = remember {
                            it.arguments?.getInt("movie_id")
                        }
                        if (movie_id != null) {
                            MovieDetailsScreen(
                                movie_id = movie_id,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}