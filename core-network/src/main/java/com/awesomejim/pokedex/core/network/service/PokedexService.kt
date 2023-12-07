package com.awesomejim.pokedex.core.network.service

import com.awesomejim.pokedex.core.network.model.PokemonInfoResponse
import com.awesomejim.pokedex.core.network.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by Awesome Jim on.
 * 07/12/2023
 */

interface PokedexService {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
    ): Response<PokemonResponse>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonInfo(@Path("name") name: String): Response<PokemonInfoResponse>
}
