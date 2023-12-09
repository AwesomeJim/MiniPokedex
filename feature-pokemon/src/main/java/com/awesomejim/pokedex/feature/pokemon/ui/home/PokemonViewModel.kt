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

package com.awesomejim.pokedex.feature.pokemon.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.pokedex.core.data.local.PokemonRepository
import com.awesomejim.pokedex.core.data.repository.ApiResult
import com.awesomejim.pokedex.core.data.repository.toResourceId
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.feature.pokemon.ui.home.PokemonUiState.Error
import com.awesomejim.pokedex.feature.pokemon.ui.home.PokemonUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

const val TIMEOUT_MILLIS = 5_000L

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val pokemonFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    private val _pokemonUiState =
        MutableStateFlow<PokemonUiState>(PokemonUiState.Loading)

    val pokemonUiState: StateFlow<PokemonUiState> =
        _pokemonUiState.asStateFlow()


    init {
        fetchPokemonData()
    }

    fun fetchPokemonData() {
        viewModelScope.launch {
            val result = pokemonRepository.fetchPokemonList(1)
            _pokemonUiState.emit(processCurrentWeatherResult(result))
        }
    }


    private fun processCurrentWeatherResult(result: ApiResult<List<Pokemon>>):
            PokemonUiState {
        return when (result) {
            is ApiResult.Success -> {
                val pokemonData = result.data
                Timber.e("Pokemon list result:: ${result.data}")
                Timber.i("first Pokemon :: ${pokemonData.first()}")
                Success(result.data)
            }

            is ApiResult.Error -> {
                Timber.e("Error :: ${result.errorType.toResourceId()}")
                Error(result.errorType.toResourceId())
            }
        }
    }

    fun addPokemon(pokemon: Pokemon) {
        viewModelScope.launch {
            pokemonRepository.add(pokemon)
        }
    }
}

sealed interface PokemonUiState {
    object Loading : PokemonUiState
    data class Error(val errorMessageId: Int) : PokemonUiState
    data class Success(val data: List<Pokemon> = listOf()) : PokemonUiState
}
