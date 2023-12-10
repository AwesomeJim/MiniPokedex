package com.awesomejim.pokedex.core.network.model


import com.awesomejim.pokedex.core.model.Pokemon
import com.awesomejim.pokedex.core.model.getImageUrl
import java.util.Locale

/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */

data class ClientException(override val message: String) : Throwable(message = message)

data class ServerException(override val message: String) : Throwable(message = message)

data class GenericException(override val message: String) : Throwable(message = message)


fun PokemonItemResponse.toCoreModel(): Pokemon {
    val index = url.split("/".toRegex()).dropLast(1).last()
    val id = index.toInt()
    val imageUrl = getImageUrl(index)
    return Pokemon(
        page,
        name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
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


