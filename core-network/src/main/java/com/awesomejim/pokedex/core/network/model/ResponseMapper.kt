package com.awesomejim.pokedex.core.network.model


import com.awesomejim.pokedex.core.model.Ability
import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.model.PokemonInfo
import com.awesomejim.pokedex.core.model.PokemonType
import com.awesomejim.pokedex.core.model.Stats
import com.awesomejim.pokedex.core.model.getImageUrl
import java.util.Locale

/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */

const val MAX_HP = 300
const val MAX_ATTACK = 300
const val MAX_DEFENSE = 300
const val MAX_SPEED = 300
const val MAX_EXP = 1000

data class ClientException(override val message: String) : Throwable(message = message)

data class ServerException(override val message: String) : Throwable(message = message)

data class GenericException(override val message: String) : Throwable(message = message)


fun PokemonItemResponse.toCoreModel(): Pokemon {
    val index = url.split("/".toRegex()).dropLast(1).last()
    val id = index.toInt()
    val imageUrl = getImageUrl(index)
    return Pokemon(
        page,
        name.titleCase(),
        id,
        imageUrl
    )
}


fun List<PokemonItemResponse>.toCoreModelList(): List<Pokemon> {
    return this.map { pokemon ->
        pokemon.toCoreModel()
    }
}

fun mapResponseCodeToThrowable(code: Int): Throwable = when (code) {
    in 400..499 -> ClientException("Client error : $code")
    in 500..600 -> ServerException("Server error : $code")
    else -> GenericException("Generic error : $code")
}


fun String.titleCase() =
    this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }


fun PokemonInfoResponse.toCoreModel(): PokemonInfo {
    val typeResponse = this.types
    val abilityResponse = this.abilities
    val statsResponse = this.stats

    val pokemonTypes = typeResponse.map {
        PokemonType(
            slot = it.slot,
            name = it.type.name.titleCase()
        )
    }
    val abilities = abilityResponse.map {
        Ability(
            name = it.ability.name.titleCase(),
            isHidden = it.isHidden,
            slot = it.slot
        )
    }

    val statistics = statsResponse.map {
        val baseStat = it.baseStat
        val formattedBaseStat = when (it.stat.name) {
            "hp" -> " $baseStat/$MAX_HP"
            "attack" -> " $baseStat/$MAX_ATTACK"
            "defense" -> " $baseStat/$MAX_DEFENSE"
            "speed" -> " $baseStat/$MAX_SPEED"
            "exp" -> " $baseStat/$MAX_EXP"
            else -> {
                "$baseStat/.."
            }
        }
        Stats(
            name = it.stat.name.titleCase(),
            baseStat = it.baseStat,
            effort = it.effort,
            formattedBaseStat = formattedBaseStat
        )
    }

    return PokemonInfo(
        id = this.id,
        name = this.name.titleCase(),
        height = this.height,
        weight = this.weight,
        experience = this.experience,
        types = pokemonTypes,
        abilities = abilities,
        stats = statistics
    )

}