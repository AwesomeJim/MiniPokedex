package com.awesomejim.pokedex.core.model


/**
 * Created by Awesome Jim on.
 * 10/12/2023
 */
fun getImageUrl(pokemonId: String): String {
    return "${BuildConfig.POKE_API_ICONS_URL}$pokemonId.png"
}