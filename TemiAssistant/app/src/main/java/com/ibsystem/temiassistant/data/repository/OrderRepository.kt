package com.ibsystem.temiassistant.data.repository

import com.ibsystem.temiassistant.data.datasource.ApiResult
import com.ibsystem.temiassistant.data.datasource.OrderDataSource
import com.ibsystem.temiassistant.domain.model.OrderModelItem
import io.github.jan.supabase.realtime.PostgresAction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface OrderRepository {
    fun getAllOrders(): Flow<ApiResult<List<OrderModelItem>>>
    fun getOrderDetailsByID(id: String): Flow<ApiResult<OrderModelItem>>
    fun updateOrderStatus(id: String, newStatus: String): Flow<ApiResult<Unit>>
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

    override fun updateOrderStatus(id: String, newStatus: String): Flow<ApiResult<Unit>> {
        return dataSource.updateOrderStatus(id, newStatus)
    }

    override suspend fun listenToOrdersChange(): Flow<PostgresAction> {
        return dataSource.listenToOrdersChange()
    }



}