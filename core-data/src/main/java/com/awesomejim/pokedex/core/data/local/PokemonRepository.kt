package com.awesomejim.pokedex.core.data.local

import com.awesomejim.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow


/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */
interface PokemonRepository {
    val pokemons: Flow<List<Pokemon>?>

    suspend fun add(pokemon: Pokemon)
}