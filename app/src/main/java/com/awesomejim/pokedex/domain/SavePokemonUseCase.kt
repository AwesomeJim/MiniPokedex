package com.awesomejim.pokedex.domain

import com.awesomejim.pokedex.core.data.local.PokemonRepository
import com.awesomejim.pokedex.core.model.Pokemon
import javax.inject.Inject


/**
 * Created by Awesome Jim on.
 * 11/12/2023
 */
class SavePokemonUseCase @Inject constructor (private val pokemonRepository: PokemonRepository) {

    suspend operator fun invoke(pokemon: Pokemon){
        return pokemonRepository.add(pokemon)
    }
}