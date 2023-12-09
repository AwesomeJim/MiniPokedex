package com.awesomejim.pokedex.core.network.model

import com.awesomejim.pokedex.core.model.Pokemon


/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */

fun PokemonItemResponse.toCoreModel(): Pokemon {
    val index = url.split("/".toRegex()).dropLast(1).last()
    val id = index.toInt()
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
            "pokemon/other/official-artwork/$index.png"
    return Pokemon(
        page,
        name,
        id,
        imageUrl
    )
}


fun List<PokemonItemResponse>.toCoreModelList(): List<Pokemon> {
    return this.map { pokemon ->
        pokemon.toCoreModel()
    }
}