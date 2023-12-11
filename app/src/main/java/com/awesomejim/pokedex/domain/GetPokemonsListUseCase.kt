package com.awesomejim.pokedex.domain

import com.awesomejim.pokedex.core.data.local.PokemonRepository
import com.awesomejim.pokedex.core.data.repository.ApiResult
import com.awesomejim.pokedex.core.model.Pokemon
import javax.inject.Inject


/**
 * Created by Awesome Jim on.
 * 11/12/2023
 */
class GetPokemonsListUseCase @Inject constructor (private val pokemonRepository: PokemonRepository) {

    suspend operator fun invoke(): ApiResult<List<Pokemon>> {
        return pokemonRepository.fetchPokemonList(1)
    }
}