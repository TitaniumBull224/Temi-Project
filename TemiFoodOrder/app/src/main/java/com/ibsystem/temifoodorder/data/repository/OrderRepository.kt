package com.ibsystem.temifoodorder.data.repository

import com.ibsystem.temifoodorder.data.datasource.OrderDataSource
import com.ibsystem.temifoodorder.domain.model.InsertParam
import com.ibsystem.temifoodorder.domain.model.OrderItem
import com.ibsystem.temifoodorder.domain.model.OrderDetailItem
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.utils.ApiResult
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface OrderRepository {
    fun getAllOrders(tableID: String): Flow<ApiResult<List<OrderDetailItem>>>
    fun insertNewOrder(orderItem: OrderItem): Flow<ApiResult<List<OrderItem>>>
    fun getOrderDetailsByID(id: String): Flow<ApiResult<OrderDetailItem>>
    fun updateOrderStatus(id: String, newStatus: String): Flow<ApiResult<Unit>>
    fun addNewOrderProductDetails(insertParam: InsertParam): Flow<ApiResult<Unit>>
    fun getAllProducts(): Flow<ApiResult<List<ProductItem>>>

    fun getRealtimeStatus(): Realtime.Status
    suspend fun listenToChange(channelName: String ,tableName: String): Flow<PostgresAction>
}

class OrderRepositoryImpl @Inject constructor(
    private val dataSource: OrderDataSource
) : OrderRepository {
    override fun getAllOrders(tableID: String): Flow<ApiResult<List<OrderDetailItem>>> {
        return dataSource.getAllOrders(tableID)
    }

    override fun insertNewOrder(orderItem: OrderItem): Flow<ApiResult<List<OrderItem>>> {
        return dataSource.insertNewOrder(orderItem)
    }

    override fun getOrderDetailsByID(id: String): Flow<ApiResult<OrderDetailItem>> {
        return dataSource.getOrderDetailsByID(id)
    }

    override fun updateOrderStatus(id: String, newStatus: String): Flow<ApiResult<Unit>> {
        return dataSource.updateOrderStatus(id, newStatus)
    }

    override fun addNewOrderProductDetails(insertParam: InsertParam): Flow<ApiResult<Unit>> {
        return dataSource.addNewOrderProductDetails(insertParam)
    }

    override suspend fun listenToChange(channelName: String ,tableName: String): Flow<PostgresAction> {
        return dataSource.listenToChange(channelName, tableName)
    }
    override fun getAllProducts(): Flow<ApiResult<List<ProductItem>>> {
        return dataSource.getAllProducts()
    }

    override fun getRealtimeStatus(): Realtime.Status {
        return dataSource.getRealtimeStatus()
    }
}