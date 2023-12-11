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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.domain.DeletePokemonUseCase
import com.awesomejim.pokedex.domain.GetSavedPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedPokemonViewModel @Inject constructor(
    private val savedPokemonListUseCase: GetSavedPokemonListUseCase,
    private val deletePokemonUseCase: DeletePokemonUseCase
) : ViewModel() {

    val uiState: StateFlow<PokemonSavedUiState> = savedPokemonListUseCase
        .invoke().map { list ->
            if (list != null) {
                PokemonSavedUiState.Success(data = list)
            } else {
                PokemonSavedUiState.Success()
            }
        }
        .catch {
            PokemonSavedUiState.Error(it)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            PokemonSavedUiState.Loading
        )


    fun addOrDeletePokemon(pokemon: Pokemon,isFavorite:Boolean) {
        viewModelScope.launch {
            deletePokemonUseCase.invoke(pokemon)
        }
    }
}

sealed interface PokemonSavedUiState {
    object Loading : PokemonSavedUiState
    data class Error(val message: Throwable) : PokemonSavedUiState
    data class Success(val data: List<Pokemon> = listOf()) : PokemonSavedUiState
}
