package com.awesomejim.pokedex.presentation.ui.info

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.awesomejim.pokedex.R
import com.awesomejim.pokedex.core.data.di.fakePokemonInfo
import com.awesomejim.pokedex.core.data.di.fakePokemons
import com.awesomejim.pokedex.core.model.PokemonInfo
import com.awesomejim.pokedex.core.model.Stats
import com.awesomejim.pokedex.core.ui.components.ErrorScreen
import com.awesomejim.pokedex.core.ui.components.LoadingProgressScreens
import com.awesomejim.pokedex.core.ui.components.Subtitle
import com.awesomejim.pokedex.core.ui.components.SubtitleSmall
import com.awesomejim.pokedex.core.ui.theme.PokemonTheme
import com.awesomejim.pokedex.presentation.viewModels.PokemonInfoUiState
import com.awesomejim.pokedex.presentation.viewModels.PokemonInfoViewModel


/**
 * Created by Awesome Jim on.
 * 10/12/2023
 */


@Composable
fun PokemonInfoScreen(
    pokemonName: String,
    imageUrl: String,
    id: String,
    pokemonInfoViewModel: PokemonInfoViewModel
) {
    LaunchedEffect(Unit) {
        pokemonInfoViewModel.fetchPokemonInfoData(name = pokemonName)
    }
    val pokemonInfoUiState = pokemonInfoViewModel.pokemonInfoUiState
        .collectAsStateWithLifecycle().value

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(8.dp))
        InfoScreenBanner(
            pokemonName, imageUrl, id
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
    id: String
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
        //url .../42.png
        Subtitle(
            text = "#$id",
        )
    }
}

@Composable
fun PokemonInfoCard(pokemonInfo: PokemonInfo) {
    Column(horizontalAlignment = Alignment.Start) {
        BodyParamsDetailsCard(pokemonInfo)
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
            modifier = Modifier.width(150.dp)
        )
        Spacer(
            modifier = Modifier
                .width(8.dp)
                .height(8.dp)
        )
        Text(
            text = stats.baseStat.toString(),
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun BodyParamsDetailsCard(
    pokemonInfo: PokemonInfo,
    modifier: Modifier = Modifier
) {
    val weight = pokemonInfo.getWeightString()
    val height = pokemonInfo.getHeightString()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            BodyParamsSections(
                conditionText = weight,
                conditionLabel = R.string.weight_label,
                drawable = R.mipmap.ic_weight
            )
            Spacer(modifier = Modifier.width(8.dp))
            BodyParamsSections(
                conditionText = height,
                conditionLabel = R.string.height_label,
                drawable = R.mipmap.ic_height
            )
        }
    }

}

@Composable
fun BodyParamsSections(
    conditionText: String,
    @StringRes conditionLabel: Int,
    @DrawableRes drawable: Int,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(180.dp)
                .height(60.dp)
        ) {
            Text(
                text = conditionText,
                style = MaterialTheme.typography.headlineSmall,
                modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)
            )
            BodyParams(modifier, drawable, conditionLabel)
        }
    }
}

@Composable
fun BodyParams(
    modifier: Modifier,
    @DrawableRes drawable: Int,
    @StringRes paramLabel: Int
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(12.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
        )
        Text(
            text = stringResource(paramLabel),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
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
            imageUrl = pokemon.url,
            id = pokemon.id.toString()
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