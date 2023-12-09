package com.awesomejim.pokedex.core.data.repository


import com.awesomejim.pokedex.core.model.Pokemon


/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */
interface RemoteDataSource {

    suspend fun fetchPokemonList(
        page: Int,
    ): ApiResult<List<Pokemon>>
}