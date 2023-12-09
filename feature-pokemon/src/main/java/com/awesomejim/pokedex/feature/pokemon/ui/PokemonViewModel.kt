/*
 * Copyright (C) 2023, Designed and developed by awesomejim (James Mbugua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.awesomejim.pokedex.feature.pokemon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.pokedex.core.data.local.PokemonRepository
import com.awesomejim.pokedex.feature.pokemon.ui.PokemonUiState.Error
import com.awesomejim.pokedex.feature.pokemon.ui.PokemonUiState.Loading
import com.awesomejim.pokedex.feature.pokemon.ui.PokemonUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.internal.NopCollector.emit
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    val uiState: StateFlow<PokemonUiState> = pokemonRepository
        .pokemons.map<List<String>, PokemonUiState> { Success(data = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addPokemon(name: String) {
        viewModelScope.launch {
            pokemonRepository.add(name)
        }
    }
}

sealed interface PokemonUiState {
    object Loading : PokemonUiState
    data class Error(val throwable: Throwable) : PokemonUiState
    data class Success(val data: List<String>) : PokemonUiState
}
