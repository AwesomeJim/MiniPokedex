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

package com.awesomejim.pokedex.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.pokedex.core.data.repository.ApiResult
import com.awesomejim.pokedex.core.data.repository.toResourceId
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.domain.DeletePokemonUseCase
import com.awesomejim.pokedex.domain.GetPokemonsListUseCase
import com.awesomejim.pokedex.domain.SavePokemonUseCase
import com.awesomejim.pokedex.presentation.viewModels.PokemonUiState.Error
import com.awesomejim.pokedex.presentation.viewModels.PokemonUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

const val TIMEOUT_MILLIS = 5_000L

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonsListUseCase: GetPokemonsListUseCase,
    private val savePokemonUseCase: SavePokemonUseCase,
    private val deletePokemonUseCase: DeletePokemonUseCase
) : ViewModel() {

    private val pokemonFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    private val _pokemonUiState =
        MutableStateFlow<PokemonUiState>(PokemonUiState.Loading)

    val pokemonUiState: StateFlow<PokemonUiState> =
        _pokemonUiState.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    private var pokomonList: List<Pokemon> = listOf()

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
    }

    val searchResults: StateFlow<PokemonUiState> =
        snapshotFlow { searchQuery }
            .combine(_pokemonUiState) { searchQuery, pokemons ->
                when {
                    searchQuery.isNotEmpty() -> {
                        when (pokemons) {
                            is Error -> TODO()
                            PokemonUiState.Loading -> PokemonUiState.Loading
                            is Success -> {
                                val results = pokemons.data.filter { poke ->
                                    poke.name.contains(searchQuery, ignoreCase = true)
                                }
                                PokemonUiState.Success(results)
                            }
                        }
                    }
                    else -> PokemonUiState.Success(pokomonList)
                }

            }.stateIn(
                scope = viewModelScope,
                initialValue = PokemonUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    init {
        fetchPokemonData()
    }

    fun fetchPokemonData() {
        viewModelScope.launch {
            val result = getPokemonsListUseCase.invoke()
            _pokemonUiState.emit(processPokemonListResult(result))
        }
    }


    private fun processPokemonListResult(result: ApiResult<List<Pokemon>>):
            PokemonUiState {
        return when (result) {
            is ApiResult.Success -> {
                val pokemonData = result.data
                Timber.e("Pokemon list result:: ${result.data}")
                Timber.i("first Pokemon :: ${pokemonData.first()}")
                pokomonList = result.data
                Success(result.data)
            }

            is ApiResult.Error -> {
                Timber.e("Error :: ${result.errorType.toResourceId()}")
                Error(result.errorType.toResourceId())
            }
        }
    }

    fun addOrDeletePokemon(pokemon: Pokemon, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                savePokemonUseCase.invoke(pokemon)
            } else {
                deletePokemonUseCase.invoke(pokemon)
            }
        }
    }
}

sealed interface PokemonUiState {
    data object Loading : PokemonUiState
    data class Error(val errorMessageId: Int) : PokemonUiState
    data class Success(val data: List<Pokemon> = listOf()) : PokemonUiState
}
