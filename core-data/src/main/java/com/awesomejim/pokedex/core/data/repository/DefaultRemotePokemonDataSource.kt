package com.awesomejim.pokedex.core.data.repository

import androidx.annotation.VisibleForTesting
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.network.model.mapResponseCodeToThrowable
import com.awesomejim.pokedex.core.network.model.toCoreModelList
import com.awesomejim.pokedex.core.network.service.PokedexClient
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */
@VisibleForTesting
class DefaultRemotePokemonDataSource @Inject constructor(
    private val pokedexClient: PokedexClient
) : RemoteDataSource {
    override suspend fun fetchPokemonList(page: Int): ApiResult<List<Pokemon>> =
        try {
            val response = pokedexClient.fetchPokemonList(page)
            if (response.isSuccessful && response.body() != null) {
                val pokemonList = response.body()!!.results.toCoreModelList()
                ApiResult.Success(data = pokemonList)
            } else {
                throw mapResponseCodeToThrowable(response.code())
            }
        } catch (e: Exception) {
            Timber.e(
                "<<<<<<<<<fetchWeatherDataWithCoordinates Exception>>>>>>>>>>: %s",
                e.message
            )
            printTrace(e)
            throw e
        }

}