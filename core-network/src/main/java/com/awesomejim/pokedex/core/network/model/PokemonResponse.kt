package com.awesomejim.pokedex.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Created by Awesome Jim on.
 * 07/12/2023
 */

@Serializable
data class PokemonResponse(
    @SerialName("count") val count: Int,
    @SerialName("next") val next: String?,
    @SerialName("previous") val previous: String?,
    @SerialName("results") val results: List<PokemonItemResponse>,
)

@Serializable
data class PokemonItemResponse(
    var page: Int = 0,
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
)


@Serializable
data class PokemonInfoResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("height") val height: Int,
    @SerialName("weight") val weight: Int,
    @SerialName("base_experience") val experience: Int,
    @SerialName("types") val types: List<TypeResponse>,
)


@Serializable
data class TypeResponse(
    @SerialName("slot") val slot: Int,
    @SerialName("type") val type: Type,
)

@Serializable
data class Type(
    @SerialName("name") val name: String,
)