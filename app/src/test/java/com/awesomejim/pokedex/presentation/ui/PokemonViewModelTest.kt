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

package com.awesomejim.pokedex.presentation.ui



import com.awesomejim.pokedex.core.data.local.PokemonRepository
import com.awesomejim.pokedex.core.data.repository.ApiResult
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.model.PokemonInfo
import com.awesomejim.pokedex.presentation.viewModels.PokemonUiState
import com.awesomejim.pokedex.presentation.viewModels.SavedPokemonViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PokemonViewModelTest {
    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel =
        SavedPokemonViewModel(FakePokemonRepository())
        assertEquals(viewModel.uiState.first(), PokemonUiState.Loading)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        val viewModel =
            SavedPokemonViewModel(FakePokemonRepository())
        assertEquals(viewModel.uiState.first(), PokemonUiState.Loading)
    }
}

private class FakePokemonRepository : PokemonRepository {

    private val data = mutableListOf<Pokemon>()

    override val pokemons: Flow<List<Pokemon>>
        get() = flow { emit(data.toList()) }

    override suspend fun add(pokemon: Pokemon) {
        data.add(0, pokemon)
    }

    override suspend fun delete(pokemon: Pokemon) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPokemonList(page: Int): ApiResult<List<Pokemon>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPokemonInfo(name: String): ApiResult<PokemonInfo> {
        TODO("Not yet implemented")
    }
}
