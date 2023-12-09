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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.awesomejim.pokedex.R
import com.awesomejim.pokedex.core.ui.components.PokemonTopAppBar
import com.awesomejim.pokedex.core.ui.theme.PokemonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val bottomNavigationItems = listOf(
        PokemonNavigation.Home,
        PokemonNavigation.Saved,
        PokemonNavigation.Settings
    )
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonTheme {
                navController = rememberNavController()
                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                val title = rememberSaveable { (mutableStateOf("")) }
                val canNavigateBackState = rememberSaveable { (mutableStateOf(false)) }
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                when (navBackStackEntry?.destination?.route) {
                    PokemonNavigation.PokemonInfo.routeWithArgs -> {
                        // Retrieve the passed argument
                        title.value = navBackStackEntry!!
                            .arguments?.getString(
                                PokemonNavigation
                                    .PokemonInfo
                                    .pokemonNameTypeArg
                            ) ?: "Pokemon"
                        canNavigateBackState.value = true
                        bottomBarState.value = false
                    }

                    PokemonNavigation.Home.screenRoute -> {
                        title.value =
                            stringResource(id = R.string.home_screen_title)
                        if (!bottomBarState.value) bottomBarState.value = true
                        if (canNavigateBackState.value) canNavigateBackState.value = false

                    }

                    PokemonNavigation.Saved.screenRoute -> {
                        title.value =
                            stringResource(id = R.string.saved_screen_title)
                        if (!bottomBarState.value) bottomBarState.value = true
                        if (canNavigateBackState.value) canNavigateBackState.value = false
                    }

                    PokemonNavigation.Settings.screenRoute -> {
                        title.value =
                            stringResource(id = R.string.settings_screen_title)
                        if (!bottomBarState.value) bottomBarState.value = true
                        if (canNavigateBackState.value) canNavigateBackState.value = false
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        PokemonTopAppBar(
                            title = title.value,
                            modifier = Modifier,
                            navigateUp = { navController.navigateUp() },
                            canNavigateBack = canNavigateBackState.value
                        )
                    },
                    bottomBar = {
                        if (bottomBarState.value) {
                            AppBottomNavigationItem(
                                navController = navController,
                                bottomNavigationItems
                            )
                        }
                    }

                ) { paddingValues ->
                    Surface(
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        MainNavigation(navController, paddingValues)
                    }
                }
            }
        }
    }
}
