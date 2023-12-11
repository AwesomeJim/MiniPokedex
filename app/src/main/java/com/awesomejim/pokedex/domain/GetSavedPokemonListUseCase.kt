package com.awesomejim.pokedex.domain

import com.awesomejim.pokedex.core.data.local.PokemonRepository
import com.awesomejim.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject


/**
 * Created by Awesome Jim on.
 * 11/12/2023
 */
class GetSavedPokemonListUseCase @Inject constructor (private val pokemonRepository: PokemonRepository) {

    operator fun invoke(): Flow<List<Pokemon>?> {
        return pokemonRepository.pokemons
    }
}