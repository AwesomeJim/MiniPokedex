package com.awesomejim.pokedex.core.model


/**
 * Created by Awesome Jim on.
 * 07/12/2023
 */
data class PokemonInfo(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val experience: Int,
    val types: List<PokemonType>,
    val abilities: List<Ability>,
    val stats: List<Stats>,
) {

    fun getIdString(): String = String.format("#%03d", id)
    fun getWeightString(): String = String.format("%.1f KG", weight.toFloat() / 10)
    fun getHeightString(): String = String.format("%.1f M", height.toFloat() / 10)

}
data class PokemonType(
    val slot: Int,
    val name: String,
)


data class Ability(
    val name: String,
    val isHidden:Boolean,
    val slot: Int
)

data class Stats(
    val name: String,
    val baseStat:Int,
    val effort:Int,
    val formattedBaseStat:String
)