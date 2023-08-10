package com.ibsystem.temifoodorder.data.repository

import com.ibsystem.temifoodorder.data.datasource.ProductDataSource
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProductRepository {
    fun getAllProducts(): Flow<ApiResult<List<ProductItem>>>
    //suspend fun closeConnection()
}

class ProductRepositoryImpl @Inject constructor(private val productDataSource: ProductDataSource): ProductRepository {
    override fun getAllProducts(): Flow<ApiResult<List<ProductItem>>> {
        return productDataSource.getAllProducts()
    }


}