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

package com.awesomejim.pokedex.feature.pokemon.ui.home


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.awesomejim.pokedex.core.data.di.fakePokemons
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.ui.MyApplicationTheme
import com.awesomejim.pokedex.feature.pokemon.ui.home.PokemonUiState.Success

@Composable
fun PokemonScreen(modifier: Modifier = Modifier, viewModel: PokemonViewModel = hiltViewModel()) {
    val items by viewModel.pokemonUiState.collectAsStateWithLifecycle()
    //
    val lazyListState = rememberLazyListState()
    if (items is Success) {
        PokemonScreen(
            items = (items as Success).data,
            onSave = { pokemon -> viewModel.addPokemon(pokemon) },
            modifier = modifier
        )
    }
}

@Composable
internal fun PokemonScreen(
    items: List<Pokemon>,
    onSave: (pokemon: Pokemon) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        items.forEach {
            Text(it.name)
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        PokemonScreen(fakePokemons, onSave = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        PokemonScreen(fakePokemons, onSave = {})
    }
}
