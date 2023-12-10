package com.awesomejim.pokedex.core.data.local

import com.awesomejim.pokedex.core.data.repository.ApiResult
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.model.PokemonInfo
import kotlinx.coroutines.flow.Flow


/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */
interface PokemonRepository {
    val pokemons: Flow<List<Pokemon>?>

    suspend fun add(pokemon: Pokemon)

    suspend fun delete(pokemon: Pokemon)

    suspend fun fetchPokemonList(
        page: Int,
    ): ApiResult<List<Pokemon>>

    suspend fun fetchPokemonInfo(
        name: String,
    ): ApiResult<PokemonInfo>
}