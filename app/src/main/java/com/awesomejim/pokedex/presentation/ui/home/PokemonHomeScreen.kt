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

package com.awesomejim.pokedex.presentation.ui.home


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.awesomejim.pokedex.R
import com.awesomejim.pokedex.core.data.di.fakePokemons
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.ui.theme.PokemonTheme


@Composable
fun PokemonScreen(
    items: List<Pokemon>,
    onSave: (pokemon: Pokemon, isFavorite: Boolean) -> Unit,
    onClick: (pokemon: Pokemon) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            PokemonCard(
                item,
                onSave = onSave,
                onClick = onClick,
                modifier
            )
        }
    }
}

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onSave: (pokemon: Pokemon, isFavorite: Boolean) -> Unit,
    onClick: (pokemon: Pokemon) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .animateContentSize()
            .clickable {
                onClick(pokemon)
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            ) {
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
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(6.dp)
                        .size(32.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    FavoriteButton(
                        pokemon = pokemon,
                        onSave = onSave,
                        modifier = Modifier.padding(8.dp)
                    )
                }

            }
            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Text(
                    text = "#${pokemon.id}",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }

}

@Composable
fun FavoriteButton(
    pokemon: Pokemon,
    onSave: (pokemon: Pokemon, isFavorite: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color(0xffE91E63)
) {
    var isFavorite by remember { mutableStateOf(pokemon.isFavourite) }
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            isFavorite = !isFavorite
            onSave(pokemon, isFavorite)
            pokemon.isFavourite = isFavorite
        }
    ) {
        Icon(
            tint = color,
            modifier = modifier.graphicsLayer {
                scaleX = 1.3f
                scaleY = 1.3f
            },
            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }

}
// Previews

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DefaultPreview() {
    PokemonTheme {
        PokemonScreen(fakePokemons,
            onSave = { _, _ -> },
            onClick = {})
    }
}

@Preview(showBackground = true, widthDp = 480, showSystemUi = true)
@Composable
private fun PortraitPreview() {
    PokemonTheme {
        PokemonScreen(fakePokemons,
            onSave = { _, _ -> },
            onClick = {})
    }
}
