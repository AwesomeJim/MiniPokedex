package com.awesomejim.pokedex.feature.pokemon.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awesomejim.pokedex.core.data.local.PokemonRepository
import com.awesomejim.pokedex.core.data.repository.ApiResult
import com.awesomejim.pokedex.core.data.repository.toResourceId
import com.awesomejim.pokedex.core.model.PokemonInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by Awesome Jim on.
 * 10/12/2023
 */
@HiltViewModel
class PokemonInfoViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _pokemonInfoUiState =
        MutableStateFlow<PokemonInfoUiState>(PokemonInfoUiState.Loading)

    val pokemonInfoUiState: StateFlow<PokemonInfoUiState> =
        _pokemonInfoUiState.asStateFlow()

    fun fetchPokemonInfoData(name: String) {
        viewModelScope.launch {
            val result = pokemonRepository.fetchPokemonInfo(name)
            _pokemonInfoUiState.emit(processPokemonInfoResult(result))
        }
    }

    private fun processPokemonInfoResult(result: ApiResult<PokemonInfo>):
            PokemonInfoUiState {
        return when (result) {
            is ApiResult.Success -> {
                val pokemonData = result.data
                Timber.e("Pokemon info result:: ${result.data}")
                Timber.i(" Pokemon :: ${pokemonData.name}")
                PokemonInfoUiState.Success(result.data)
            }

            is ApiResult.Error -> {
                Timber.e("Error :: ${result.errorType.toResourceId()}")
                PokemonInfoUiState.Error(result.errorType.toResourceId())
            }
        }
    }
}


sealed interface PokemonInfoUiState {
    object Loading : PokemonInfoUiState
    data class Error(val errorMessageId: Int) : PokemonInfoUiState
    data class Success(val data: PokemonInfo?) : PokemonInfoUiState
}