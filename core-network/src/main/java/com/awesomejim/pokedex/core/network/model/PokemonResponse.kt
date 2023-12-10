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
    @SerialName("types") val types: List<TypesResponse>,
    @SerialName("abilities") val abilities: List<AbilitiesResponse>,
    @SerialName("stats") val stats: List<StatsResponse>
)


@Serializable
data class TypesResponse(
    @SerialName("slot") val slot: Int,
    @SerialName("type") val type: TypeResponse,
)

@Serializable
data class TypeResponse(
    @SerialName("name") val name: String,
)

@Serializable
data class AbilitiesResponse(
    @SerialName("slot") val slot: Int,
    @SerialName("is_hidden") val isHidden: Boolean,
    @SerialName("ability") val ability: AbilityResponse,
)

@Serializable
data class AbilityResponse(
    @SerialName("name") val name: String,
)

@Serializable
data class StatsResponse(
    @SerialName("base_stat") val baseStat: Int,
    @SerialName("effort") val effort: Int,
    @SerialName("stat") val stat: StatResponse,
)

@Serializable
data class StatResponse(
    @SerialName("name") val name: String,
)