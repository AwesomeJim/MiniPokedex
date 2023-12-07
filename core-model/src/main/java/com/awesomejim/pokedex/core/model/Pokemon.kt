package com.awesomejim.pokedex.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Created by Awesome Jim on.
 * 07/12/2023
 */
@Parcelize
data class Pokemon(
    var page: Int = 0,
    val name: String,
    val url: String,
) : Parcelable {
    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
                "pokemon/other/official-artwork/$index.png"
    }
}