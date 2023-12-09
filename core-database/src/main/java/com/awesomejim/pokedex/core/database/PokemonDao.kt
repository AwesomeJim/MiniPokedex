package com.awesomejim.pokedex.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(item: PokemonEntity)
}