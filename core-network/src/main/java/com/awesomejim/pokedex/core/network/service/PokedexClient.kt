package com.awesomejim.pokedex.core.network.service

import com.awesomejim.pokedex.core.network.model.PokemonInfoResponse
import com.awesomejim.pokedex.core.network.model.PokemonResponse
import retrofit2.Response
import javax.inject.Inject


/**
 * Created by Awesome Jim on.
 * 07/12/2023
 */
class PokedexClient @Inject constructor(
    private val pokedexService: PokedexService,
) {

    suspend fun fetchPokemonList(page: Int): Response<PokemonResponse> =
        pokedexService.fetchPokemonList(
            limit = PAGING_SIZE,
            offset = page * PAGING_SIZE,
        )

    suspend fun fetchPokemonInfo(name: String): Response<PokemonInfoResponse> =
        pokedexService.fetchPokemonInfo(
            name = name,
        )

    companion object {
        private const val PAGING_SIZE = 20
    }
}