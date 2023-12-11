/*
 * Copyright (C) 2023, Designed and developed by awesomejim (James Mbugua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.awesomejim.pokedex.presentation.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.model.getImageUrl
import com.awesomejim.pokedex.core.ui.components.ErrorScreen
import com.awesomejim.pokedex.core.ui.components.LoadingProgressScreens
import com.awesomejim.pokedex.presentation.ui.PokemonNavigation.PokemonInfo.pokemonIdTypeArg
import com.awesomejim.pokedex.presentation.ui.PokemonNavigation.PokemonInfo.pokemonNameTypeArg
import com.awesomejim.pokedex.presentation.ui.home.PokemonScreen
import com.awesomejim.pokedex.presentation.ui.info.PokemonInfoScreen
import com.awesomejim.pokedex.presentation.ui.saved.SavedPokemonScreen
import com.awesomejim.pokedex.presentation.viewModels.PokemonInfoViewModel
import com.awesomejim.pokedex.presentation.viewModels.PokemonSavedUiState
import com.awesomejim.pokedex.presentation.viewModels.PokemonUiState
import com.awesomejim.pokedex.presentation.viewModels.PokemonViewModel
import com.awesomejim.pokedex.presentation.viewModels.SavedPokemonViewModel


sealed class PokemonNavigation(var title: String, var icon: ImageVector, var screenRoute: String) {

    object Home : PokemonNavigation("Pokemons", Icons.Filled.Home, "home")
    object PokemonInfo : PokemonNavigation("Info", Icons.Filled.Home, "info") {
        const val pokemonNameTypeArg = "name"
        const val pokemonIdTypeArg = "id"
        val routeWithArgs =
            "$screenRoute/{$pokemonNameTypeArg}/{$pokemonIdTypeArg}"
        val arguments = listOf(
            navArgument(pokemonNameTypeArg) { type = NavType.StringType },
            navArgument(pokemonIdTypeArg) { type = NavType.IntType })
    }

    object Saved : PokemonNavigation("Saved Pokemons", Icons.Filled.Favorite, "saved")
    object Settings : PokemonNavigation("Settings", Icons.Filled.Settings, "settings")

}

/**
 * App BottomNavigation item
 *
 * @param navController
 */
@Composable
fun AppBottomNavigationItem(
    navController: NavController,
    items: List<PokemonNavigation>
) {

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 9.sp
                    )
                },
                alwaysShowLabel = true,
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun MainNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController, startDestination = PokemonNavigation.Home.screenRoute,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(PokemonNavigation.Home.screenRoute) {
            val pokemonViewModel = hiltViewModel<PokemonViewModel>()
            val pokemonUiState = pokemonViewModel.pokemonUiState
                .collectAsStateWithLifecycle().value
            when (pokemonUiState) {
                is PokemonUiState.Loading -> {
                    LoadingProgressScreens()
                }

                is PokemonUiState.Error -> {
                    ErrorScreen(
                        pokemonUiState.errorMessageId,
                        onTryAgainClicked = {
                            pokemonViewModel.fetchPokemonData()
                        }
                    )
                }

                is PokemonUiState.Success -> {
                    PokemonScreen(
                        pokemonUiState.data,
                        onSave = { pokemon, isFavorite ->
                            pokemonViewModel.addOrDeletePokemon(pokemon, isFavorite)
                        },
                        onClick = {
                            navController.navigateToViewPkemonInfo(it)
                        },
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
        composable(PokemonNavigation.Saved.screenRoute) {
            val savedPokemonViewModel = hiltViewModel<SavedPokemonViewModel>()
            val pokemonUiState = savedPokemonViewModel.uiState
                .collectAsStateWithLifecycle().value
            when (pokemonUiState) {
                is PokemonSavedUiState.Loading -> {
                    LoadingProgressScreens()
                }

                is PokemonSavedUiState.Error -> {

                }

                is PokemonSavedUiState.Success -> {
                    SavedPokemonScreen(
                        items = pokemonUiState.data,
                        onDelete = { pokemon: Pokemon, isFavorite: Boolean ->
                            savedPokemonViewModel.addOrDeletePokemon(pokemon, isFavorite)
                        },
                        onClick = {
                            navController.navigateToViewPkemonInfo(it)
                        }
                    )
                }
            }
        }
        composable(PokemonNavigation.Settings.screenRoute) {


        }
        composable(
            PokemonNavigation.PokemonInfo.routeWithArgs,
            arguments = PokemonNavigation.PokemonInfo.arguments,
            enterTransition = {
                slideInVertically(
                    animationSpec = tween(700),
                    initialOffsetY = { it }
                )
            },
            exitTransition = {
                slideOutVertically(
                    animationSpec = tween(700),
                    targetOffsetY = { it }
                )
            }
        )
        { navBackStackEntry ->
            val pokemonInfoViewModel = hiltViewModel<PokemonInfoViewModel>()

            // Retrieve the passed argument
            val pokemonName =
                navBackStackEntry.arguments?.getString(pokemonNameTypeArg) ?: "Pokemon"
            val id =
                navBackStackEntry.arguments?.getInt(pokemonIdTypeArg) ?: 1
            val imageUrl = getImageUrl(id.toString())
            PokemonInfoScreen(
                pokemonName = pokemonName,
                imageUrl = imageUrl,
                pokemonInfoViewModel = pokemonInfoViewModel
            )
        }
    }
}

private fun NavHostController.navigateToViewPkemonInfo(pokemon: Pokemon) {
    val pokemonNameTypeArg = pokemon.name
    val pokemonIdTypeArg = pokemon.id
    this.navigate(
        "${
            PokemonNavigation.PokemonInfo
                .screenRoute
        }/$pokemonNameTypeArg/$pokemonIdTypeArg"
    )
}