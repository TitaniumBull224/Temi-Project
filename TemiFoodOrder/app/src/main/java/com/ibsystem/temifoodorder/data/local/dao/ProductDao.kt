package com.ibsystem.temifoodorder.data.local.dao

import androidx.room.*
import com.ibsystem.temifoodorder.domain.model.ProductItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductItem>)

    @Query("SELECT * FROM product_table")
    fun getAllProducts(): Flow<List<ProductItem>>

    @Query("SELECT * FROM product_table WHERE id=:productId")
    fun getSelectedProduct(productId: Int): ProductItem

    @Query("SELECT * FROM product_table WHERE id=1")
    fun getAllProductCart(): Flow<List<ProductItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCart(productItem: ProductItem)

    @Update
    suspend fun deleteCart(productItem: ProductItem)

    @Query("SELECT * FROM product_table WHERE name LIKE '%' || :query || '%'")
    fun searchProduct(query: String): Flow<List<ProductItem>>

}