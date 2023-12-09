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

package com.awesomejim.pokedex.core.data

import com.awesomejim.pokedex.core.database.PokemonDao
import com.awesomejim.pokedex.core.database.entitiy.mapper.asDomainList
import com.awesomejim.pokedex.core.database.entitiy.mapper.asEntity
import com.awesomejim.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface PokemonRepository {
    val pokemons: Flow<List<Pokemon>?>

    suspend fun add(pokemon:Pokemon)
}

class DefaultPokemonRepository @Inject constructor(
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override val pokemons: Flow<List<Pokemon>?> =
        pokemonDao.getPokemonList().map {
            it?.asDomainList()
       }

    override suspend fun add(pokemon:Pokemon) {
        pokemonDao.insertPokemon(pokemon.asEntity())
    }
}
