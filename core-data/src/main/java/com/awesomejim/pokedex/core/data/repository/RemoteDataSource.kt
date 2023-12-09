package com.awesomejim.pokedex.core.data.repository

import androidx.annotation.WorkerThread
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.network.interceptor.ApiResult


/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */
interface RemoteDataSource {
    @WorkerThread
    suspend fun fetchPokemonList(
        page: Int,
    ): ApiResult<List<Pokemon>>
}