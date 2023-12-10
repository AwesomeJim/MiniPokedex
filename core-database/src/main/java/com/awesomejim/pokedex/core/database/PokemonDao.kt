package com.awesomejim.pokedex.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.awesomejim.pokedex.core.database.entitiy.PokemonEntity
import kotlinx.coroutines.flow.Flow


/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */
@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemonentity ORDER BY id DESC LIMIT 100")
    fun getPokemonList(): Flow<List<PokemonEntity>?>

    @Upsert
    suspend fun insertPokemon(item: PokemonEntity)

    @Delete
    suspend fun deletePokemon(item: PokemonEntity)
}