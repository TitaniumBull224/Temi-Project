package com.ibsystem.temifoodorder.data.repository

import com.ibsystem.temifooddelivery.data.datasource.ApiResult
import com.ibsystem.temifoodorder.data.datasource.ProductDataSource
import com.ibsystem.temifoodorder.domain.model.OrderModelItem
import com.ibsystem.temifoodorder.domain.model.ProductItem
import io.github.jan.supabase.realtime.PostgresAction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProductRepository {
    fun getAllProducts(): Flow<ApiResult<List<ProductItem>>>
}

class ProductRepositoryImpl @Inject constructor(private val productDataSource: ProductDataSource): ProductRepository {
    override fun getAllProducts(): Flow<ApiResult<List<ProductItem>>> {
        return productDataSource.getAllProducts()
    }

}