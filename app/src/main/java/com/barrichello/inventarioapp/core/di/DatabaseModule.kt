package com.barrichello.inventarioapp.core.di

import android.content.Context
import androidx.room.Room
import com.barrichello.inventarioapp.data.local.db.InventarioDao
import com.barrichello.inventarioapp.data.local.db.InventarioDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideInventarioDatabase(
        @ApplicationContext context: Context
    ): InventarioDatabase {
        return Room.databaseBuilder(
            context,
            InventarioDatabase::class.java,
            "inventario_aco.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideInventarioDao(database: InventarioDatabase): InventarioDao {
        return database.inventarioDao()
    }
}