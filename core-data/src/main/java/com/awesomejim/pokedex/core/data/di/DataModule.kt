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

package com.awesomejim.pokedex.core.data.di

import com.awesomejim.pokedex.core.data.DefaultPokemonRepository
import com.awesomejim.pokedex.core.data.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsPokemonRepository(
        pokemonRepository: DefaultPokemonRepository
    ): PokemonRepository
}

class FakePokemonRepository @Inject constructor() : PokemonRepository {
    override val pokemons: Flow<List<String>> = flowOf(fakePokemons)

    override suspend fun add(name: String) {
        throw NotImplementedError()
    }
}

val fakePokemons = listOf("One", "Two", "Three")
