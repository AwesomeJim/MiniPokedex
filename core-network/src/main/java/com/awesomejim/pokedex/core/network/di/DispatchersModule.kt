package com.awesomejim.pokedex.core.network.di

import com.awesomejim.pokedex.core.network.Dispatcher
import com.awesomejim.pokedex.core.network.PokedexAppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 * Created by Awesome Jim on.
 * 07/12/2023
 */


@Module
@InstallIn(SingletonComponent::class)
internal object DispatchersModule {

    @Provides
    @Dispatcher(PokedexAppDispatchers.IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
