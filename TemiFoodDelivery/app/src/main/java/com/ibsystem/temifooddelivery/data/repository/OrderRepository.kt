package com.ibsystem.temifooddelivery.data.repository

import com.ibsystem.temifooddelivery.data.datasource.ApiResult
import com.ibsystem.temifooddelivery.data.datasource.OrderDataSource
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import io.github.jan.supabase.realtime.PostgresAction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface OrderRepository {
    fun getAllOrders(): Flow<ApiResult<List<OrderModelItem>>>
    fun getOrderDetailsByID(id: String): Flow<ApiResult<OrderModelItem>>
    suspend fun listenToOrdersChange(): Flow<PostgresAction>

}

class OrderRepositoryImpl @Inject constructor(
    private val dataSource: OrderDataSource
) : OrderRepository {
    override fun getAllOrders(): Flow<ApiResult<List<OrderModelItem>>> {
        return dataSource.getAllOrders()
    }

    override fun getOrderDetailsByID(id: String): Flow<ApiResult<OrderModelItem>> {
        return dataSource.getOrderDetailsByID(id)
    }

    override suspend fun listenToOrdersChange(): Flow<PostgresAction> {
        return dataSource.listenToOrdersChange()
    }

}