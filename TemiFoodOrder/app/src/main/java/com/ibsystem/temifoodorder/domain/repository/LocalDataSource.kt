package com.ibsystem.temifoodorder.domain.repository

import com.ibsystem.temifoodorder.domain.model.ProductItem
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertProducts(products: List<ProductItem>)
    fun getAllProduct(): Flow<List<ProductItem>>
    suspend fun getSelectedProduct(productId: Int): ProductItem
    fun getAllProductCart(isCart: Boolean): Flow<List<ProductItem>>
    suspend fun addCart(productItem: ProductItem)
    suspend fun deleteCart(productItem: ProductItem)
    fun searchProduct(query: String): Flow<List<ProductItem>>
}