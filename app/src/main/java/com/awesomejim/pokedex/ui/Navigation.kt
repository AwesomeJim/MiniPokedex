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

package com.awesomejim.pokedex.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import com.awesomejim.pokedex.core.ui.components.ErrorScreen
import com.awesomejim.pokedex.core.ui.components.LoadingProgressScreens
import com.awesomejim.pokedex.feature.pokemon.ui.home.PokemonScreen
import com.awesomejim.pokedex.feature.pokemon.ui.home.PokemonUiState
import com.awesomejim.pokedex.feature.pokemon.ui.home.PokemonViewModel


sealed class PokemonNavigation(var title: String, var icon: ImageVector, var screenRoute: String) {

    object Home : PokemonNavigation("Pokemons", Icons.Filled.Home, "home")
    object PokemonInfo : PokemonNavigation("Info", Icons.Filled.Home, "info") {
        const val pokemonNameTypeArg = "name"
        const val imageUrlTypeArg = "url"
        val routeWithArgs =
            "$screenRoute/{$pokemonNameTypeArg}/{$imageUrlTypeArg}"
        val arguments = listOf(
            navArgument(pokemonNameTypeArg) { type = NavType.StringType },
            navArgument(imageUrlTypeArg) { type = NavType.StringType })
    }

    object Saved : PokemonNavigation("Saved Pokemons", Icons.Filled.Home, "saved")
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
                        onSave = {
                            pokemonViewModel.addPokemon(it)
                        },
                        onClick = {

                        },
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
        composable(PokemonNavigation.Saved.screenRoute) {


        }
        composable(PokemonNavigation.Settings.screenRoute) {


        }
        composable(PokemonNavigation.PokemonInfo.screenRoute) {


        }

    }
}
