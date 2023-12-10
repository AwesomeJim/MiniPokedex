package com.awesomejim.pokedex.feature.pokemon.ui.info

import androidx.lifecycle.ViewModel
import com.awesomejim.pokedex.core.data.local.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * Created by Awesome Jim on.
 * 10/12/2023
 */
@HiltViewModel
class PokemonInfoViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {
}