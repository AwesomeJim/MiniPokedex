package com.awesomejim.pokedex.core.model


/**
 * Created by Awesome Jim on.
 * 10/12/2023
 */
fun getImageUrl(pokemonId: String): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
            "pokemon/other/official-artwork/$pokemonId.png"
}