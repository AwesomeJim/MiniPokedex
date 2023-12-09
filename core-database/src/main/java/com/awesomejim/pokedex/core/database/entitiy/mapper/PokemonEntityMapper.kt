package com.awesomejim.pokedex.core.database.entitiy.mapper

import com.awesomejim.pokedex.core.database.entitiy.PokemonEntity
import com.awesomejim.pokedex.core.model.Pokemon


/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */
object PokemonEntityMapper : EntityMapper<List<Pokemon>, List<PokemonEntity>> {

    override fun asEntityList(domain: List<Pokemon>): List<PokemonEntity> {
        return domain.map { pokemon ->
            PokemonEntity(
                page = pokemon.page,
                id = pokemon.id,
                name = pokemon.name,
                url = pokemon.url,
            )
        }
    }

    override fun asDomainList(entity: List<PokemonEntity>): List<Pokemon> {
        return entity.map { pokemonEntity ->
            Pokemon(
                page = pokemonEntity.page,
                id = pokemonEntity.id,
                name = pokemonEntity.name,
                url = pokemonEntity.url,
            )
        }
    }
}

fun List<Pokemon>.asEntityList(): List<PokemonEntity> {
    return PokemonEntityMapper.asEntityList(this)
}

fun List<PokemonEntity>?.asDomainList(): List<Pokemon> {
    return PokemonEntityMapper.asDomainList(this.orEmpty())
}

fun Pokemon.asEntity(): PokemonEntity = PokemonEntity(
    this.page,
    this.id,
    this.name,
    this.url
)
