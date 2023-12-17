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

package com.awesomejim.pokedex.core.data.local

import com.awesomejim.pokedex.core.data.repository.ApiResult
import com.awesomejim.pokedex.core.data.repository.ErrorType
import com.awesomejim.pokedex.core.data.repository.printTrace
import com.awesomejim.pokedex.core.database.PokemonDao
import com.awesomejim.pokedex.core.database.entitiy.mapper.asDomainList
import com.awesomejim.pokedex.core.database.entitiy.mapper.asEntity
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.model.PokemonInfo
import com.awesomejim.pokedex.core.network.model.toCoreModel
import com.awesomejim.pokedex.core.network.model.toCoreModelList
import com.awesomejim.pokedex.core.network.service.PokedexClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject


/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */
class DefaultPokemonRepository @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val pokedexClient: PokedexClient
) : PokemonRepository {

    override val pokemons: Flow<List<Pokemon>?> =
        pokemonDao.getPokemonList().map {
            it?.asDomainList()
        }

    override suspend fun add(pokemon: Pokemon) {
        pokemonDao.insertPokemon(pokemon.asEntity())
    }

    override suspend fun delete(pokemon: Pokemon) {
        pokemonDao.deletePokemon(pokemon.asEntity())
    }

    override suspend fun fetchPokemonList(page: Int): ApiResult<List<Pokemon>> =
        try {
            val response = pokedexClient.fetchPokemonList(page)
            if (response.isSuccessful && response.body() != null) {
                val pokemonList = response.body()!!.results.toCoreModelList()
                ApiResult.Success(data = pokemonList)
            } else {
                ApiResult.Error(ErrorType.IO_CONNECTION)
               // throw mapResponseCodeToThrowable(response.code())
            }
        } catch (e: Exception) {
            Timber.e(
                "<<<<<<<<<fetchPokemonList Exception>>>>>>>>>>: %s",
                e.message
            )
            printTrace(e)
            //throw e
            ApiResult.Error(ErrorType.IO_CONNECTION)
        }

    override suspend fun fetchPokemonInfo(name: String): ApiResult<PokemonInfo> =
        try {
            val response = pokedexClient.fetchPokemonInfo(name.lowercase(Locale.ROOT))
            if (response.isSuccessful && response.body() != null) {
                val pokemonList = response.body()!!.toCoreModel()
                ApiResult.Success(data = pokemonList)
            } else {
                ApiResult.Error(ErrorType.IO_CONNECTION)
              //  throw mapResponseCodeToThrowable(response.code())
            }
        } catch (e: Exception) {
            Timber.e(
                "<<<<<<<<<fetchPokemonInfo Exception>>>>>>>>>>: %s",
                e.message
            )
            printTrace(e)
            ApiResult.Error(ErrorType.IO_CONNECTION)
            //throw e
        }
}
