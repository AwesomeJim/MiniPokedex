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

package com.awesomejim.pokedex.data

import com.awesomejim.pokedex.core.data.DefaultPokemonRepository
import com.awesomejim.pokedex.core.database.Pokemon
import com.awesomejim.pokedex.core.database.PokemonDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [DefaultPokemonRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultPokemonRepositoryTest {

    @Test
    fun pokemons_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultPokemonRepository(FakePokemonDao())

        repository.add("Repository")

        assertEquals(repository.pokemons.first().size, 1)
    }

}

private class FakePokemonDao : PokemonDao {

    private val data = mutableListOf<Pokemon>()

    override fun getPokemons(): Flow<List<Pokemon>> = flow {
        emit(data)
    }

    override suspend fun insertPokemon(item: Pokemon) {
        data.add(0, item)
    }
}
