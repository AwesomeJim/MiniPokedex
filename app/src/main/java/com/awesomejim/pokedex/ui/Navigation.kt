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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.awesomejim.pokedex.feature.pokemon.ui.home.PokemonScreen


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

@Composable
fun MainNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val navController = rememberNavController()

    NavHost(
        navController, startDestination = PokemonNavigation.Home.screenRoute,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(PokemonNavigation.Home.screenRoute) {
            PokemonScreen(modifier = Modifier.padding(16.dp))
        }

    }
}
