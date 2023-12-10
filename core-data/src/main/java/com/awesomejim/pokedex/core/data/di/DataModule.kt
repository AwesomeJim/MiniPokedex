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

import com.awesomejim.pokedex.core.data.local.DefaultPokemonRepository
import com.awesomejim.pokedex.core.data.local.PokemonRepository
import com.awesomejim.pokedex.core.data.repository.ApiResult
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.model.PokemonInfo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
interface DataModule {

    @Binds
    fun bindsPokemonRepository(
        pokemonRepository: DefaultPokemonRepository
    ): PokemonRepository

}

class FakePokemonRepository @Inject constructor() : PokemonRepository {
    override val pokemons: Flow<List<Pokemon>> = flowOf(fakePokemons)

    override suspend fun add(pokemon: Pokemon) {
        throw NotImplementedError()
    }

    override suspend fun fetchPokemonList(page: Int): ApiResult<List<Pokemon>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPokemonInfo(name: String): ApiResult<PokemonInfo> {
        TODO("Not yet implemented")
    }
}

val fakePokemons = listOf(
    Pokemon(
        page = 3707,
        name = "Bryan Navarro",
        id = 7599,
        url = "http://www.bing.com/search?q=etiam"
    ),
    Pokemon(
        page = 2713,
        name = "Sonya Reese",
        id = 6714,
        url = "https://search.yahoo.com/search?p=reprimique"
    ),
    Pokemon(
        page = 4743,
        name = "Joel Murray",
        id = 7124,
        url = "https://search.yahoo.com/search?p=solum"
    )
)

val fakePokemonInfo = PokemonInfo(
    id = 4,
    name = "Reinaldo Simon",
    height = 6,
    weight = 52,
    experience = 8555,
    types = listOf(),
    abilities = listOf(),
    stats = listOf()
)