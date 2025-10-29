package com.barrichello.inventarioapp.core.di

import com.barrichello.inventarioapp.data.repository.InventarioRepositoryImpl
import com.barrichello.inventarioapp.domain.repository.InventarioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindInventarioRepository(
        impl: InventarioRepositoryImpl
    ): InventarioRepository
}