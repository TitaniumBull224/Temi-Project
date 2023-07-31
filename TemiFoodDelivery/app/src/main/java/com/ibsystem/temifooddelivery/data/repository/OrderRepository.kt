package com.ibsystem.temifooddelivery.data.repository

import com.ibsystem.temifooddelivery.data.datasource.ApiResult
import com.ibsystem.temifooddelivery.data.datasource.OrderDataSource
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface OrderRepository {
    fun getAllOrders(): Flow<ApiResult<List<OrderModelItem>>>
}

class OrderRepositoryImpl @Inject constructor(
    private val dataSource: OrderDataSource
) : OrderRepository {
    override fun getAllOrders(): Flow<ApiResult<List<OrderModelItem>>> {
        return dataSource.getAllOrders()
    }

}