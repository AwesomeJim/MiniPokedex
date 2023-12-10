package com.awesomejim.pokedex.feature.pokemon.ui.info

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.awesomejim.pokedex.core.data.di.fakePokemonInfo
import com.awesomejim.pokedex.core.data.di.fakePokemons
import com.awesomejim.pokedex.core.model.PokemonInfo
import com.awesomejim.pokedex.core.model.Stats
import com.awesomejim.pokedex.core.ui.components.ErrorScreen
import com.awesomejim.pokedex.core.ui.components.LoadingProgressScreens
import com.awesomejim.pokedex.core.ui.components.Subtitle
import com.awesomejim.pokedex.core.ui.components.SubtitleSmall
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
    LaunchedEffect(Unit) {
        pokemonInfoViewModel.fetchPokemonInfoData(name = pokemonName)
    }
    val pokemonInfoUiState = pokemonInfoViewModel.pokemonInfoUiState
        .collectAsStateWithLifecycle().value

    Column(modifier = Modifier.fillMaxSize()) {
        InfoScreenBanner(
            pokemonName, imageUrl
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (pokemonInfoUiState) {
            is PokemonInfoUiState.Loading -> {
                LoadingProgressScreens()
            }

            is PokemonInfoUiState.Error -> {
                ErrorScreen(
                    pokemonInfoUiState.errorMessageId,
                    onTryAgainClicked = {
                        pokemonInfoViewModel.fetchPokemonInfoData(pokemonName)
                    }
                )
            }

            is PokemonInfoUiState.Success -> {
                PokemonInfoCard(pokemonInfoUiState.data)
            }
        }
    }
}

@Composable
fun InfoScreenBanner(
    pokemonName: String,
    imageUrl: String,
) {
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

@Composable
fun PokemonInfoCard(pokemonInfo: PokemonInfo) {
    Column(horizontalAlignment = Alignment.Start) {
        InfoDivider(name = "Type")
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                pokemonInfo.types.forEach { type ->
                    AssistChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        onClick = { /* do something*/ },
                        label = { Text(type.name) }
                    )
                }
            }
        }
        InfoDivider(name = "Abilities")
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                pokemonInfo.abilities.forEach { ability ->
                    AssistChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        onClick = { /* do something*/ },
                        label = { Text(ability.name) }
                    )
                }
            }
        }
        InfoDivider(name = "Base Statistics")
        pokemonInfo.stats.forEach { stat ->
            BaseStatisticsCard(stat)
        }
    }
}

@Composable
fun BaseStatisticsCard(stats: Stats) {
    Row(modifier = Modifier.fillMaxWidth()) {
        SubtitleSmall(
            text = stats.name,
            modifier = Modifier.width(150.dp))
        Spacer(
            modifier = Modifier
                .width(8.dp)
                .height(8.dp)
        )
        Text(
            text = stats.formattedBaseStat,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(4.dp)
        )
    }

}

@Composable
fun InfoDivider(name: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Subtitle(text = name)
    Divider(thickness = 1.dp, modifier = Modifier.padding(horizontal = 8.dp))
}

@Preview(showSystemUi = true)
@Composable
fun PokemonInfoScreenPreview() {
    PokemonTheme {
        val pokemon = fakePokemons.first()
        InfoScreenBanner(
            pokemonName = pokemon.name,
            imageUrl = pokemon.url
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PokemonInfoCardPreview() {
    PokemonTheme {
        val pokemonInfo = fakePokemonInfo
        PokemonInfoCard(pokemonInfo = pokemonInfo)
    }
}