package com.ibsystem.temifoodorder.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ibsystem.temifoodorder.utils.Constants.PRODUCT_DATABASE_TABLE

@Entity(tableName = PRODUCT_DATABASE_TABLE)
data class ProductItem(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val description: String,
    val image: Int,
    val price: Double
)