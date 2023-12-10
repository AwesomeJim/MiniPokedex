package com.awesomejim.pokedex.feature.pokemon.ui.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.awesomejim.pokedex.core.data.di.fakePokemons
import com.awesomejim.pokedex.core.ui.components.Subtitle
import com.awesomejim.pokedex.core.ui.theme.PokemonTheme
import com.awesomejim.pokedex.feature.pokemon.R


/**
 * Created by Awesome Jim on.
 * 10/12/2023
 */


@Composable
fun PokemonInfoScreen(
    pokemonName: String,
    imageUrl: String,
    pokemonInfoViewModel: PokemonInfoViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = pokemonName,
                contentScale = ContentScale.Fit,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img)
            )
            //url .../2.png
            Subtitle(
                text = "#${imageUrl.dropLast(4).last()}",
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun PokemonInfoScreenPreview() {
    PokemonTheme {
        val pokemon = fakePokemons.first()
        PokemonInfoScreen(
            pokemonName = pokemon.name,
            imageUrl = pokemon.url,
            pokemonInfoViewModel = hiltViewModel<PokemonInfoViewModel>()
        )
    }
}