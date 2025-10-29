package com.barrichello.inventarioapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [InventarioItemEntity::class], version = 1, exportSchema = false)
abstract class InventarioDatabase : RoomDatabase() {

    abstract fun inventarioDao(): InventarioDao

}
