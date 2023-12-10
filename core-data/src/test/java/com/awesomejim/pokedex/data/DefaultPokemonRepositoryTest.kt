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

import com.awesomejim.pokedex.core.data.local.DefaultPokemonRepository
import com.awesomejim.pokedex.core.database.PokemonDao
import com.awesomejim.pokedex.core.database.entitiy.PokemonEntity
import com.awesomejim.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [DefaultPokemonRepository].
 */
class DefaultPokemonRepositoryTest {

    @Test
    fun pokemons_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultPokemonRepository(FakePokemonDao())

        repository.add(Pokemon(
            page = 9210,
            name = "Valeria Christian",
            id = 2755,
            url = "http://www.bing.com/search?q=faucibus"
        ))

        assertEquals(repository.pokemons.first()?.size, 1)
    }

}

private class FakePokemonDao : PokemonDao {

    private val data = mutableListOf<PokemonEntity>()

    override fun getPokemonList(): Flow<List<PokemonEntity>> = flow {
        emit(data)
    }

    override suspend fun insertPokemon(item: PokemonEntity) {
        data.add(0, item)
    }

    override suspend fun deletePokemon(item: PokemonEntity) {
        TODO("Not yet implemented")
    }
}
