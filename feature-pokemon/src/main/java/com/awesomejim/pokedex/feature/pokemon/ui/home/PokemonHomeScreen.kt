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


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.awesomejim.pokedex.core.data.di.fakePokemons
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.ui.theme.PokemonTheme
import com.awesomejim.pokedex.feature.pokemon.R
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
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            PokemonCard(item, modifier)
        }
    }
}

@Composable
fun PokemonCard(
    pokemon: Pokemon, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .height(150.dp).fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(pokemon.url)
                    .crossfade(true)
                    .build(),
                contentDescription = pokemon.name,
                contentScale = ContentScale.Fit,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img)
            )
            Text(
                text = pokemon.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

}
// Previews

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DefaultPreview() {
    PokemonTheme {
        PokemonScreen(fakePokemons, onSave = {})
    }
}

@Preview(showBackground = true, widthDp = 480, showSystemUi = true)
@Composable
private fun PortraitPreview() {
    PokemonTheme {
        PokemonScreen(fakePokemons, onSave = {})
    }
}
