package com.awesomejim.pokedex.core.database.entitiy.mapper


/**
 * Created by Awesome Jim on.
 * 09/12/2023
 */
interface EntityMapper<Domain, Entity> {

    fun asEntityList(domain: Domain): Entity

    fun asDomainList(entity: Entity): Domain


}
