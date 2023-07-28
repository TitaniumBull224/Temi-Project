package com.ibsystem.temifoodorder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ibsystem.temifoodorder.data.local.dao.ProductDao
import com.ibsystem.temifoodorder.domain.model.ProductItem

@Database(entities = [ProductItem::class], version = 2)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

}