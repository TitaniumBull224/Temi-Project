package com.ibsystem.temifooddelivery.data.datasource

import android.util.Log
import com.ibsystem.temifoodorder.domain.model.InsertParam
import com.ibsystem.temifoodorder.domain.model.OrderItem
import com.ibsystem.temifoodorder.domain.model.OrderDetailItem
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.utils.ApiResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.createChannel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderDataSource @Inject constructor(private val supabaseClient: SupabaseClient) {
     fun getAllOrders(tableID: String): Flow<ApiResult<List<OrderDetailItem>>> {
         return flow {
             emit(ApiResult.Loading)
             try {
                 val queryRes = supabaseClient.postgrest.from("Order")
                     .select(columns = Columns.list(
                    "*",
                    "Product(*)",
                    "Order_Product(quantity)"
                     )) {
                         order(column = "time", order = Order.ASCENDING)
                         eq(column = "table_id", value = tableID)
                     }

                 val orders = queryRes.decodeList<OrderDetailItem>()
                 println(orders.toString())
                 emit(ApiResult.Success(orders))
             }
             catch (e: Exception) {
                emit(ApiResult.Error(e.message))
             }

         }
    }

    fun insertNewOrder(orderItem: OrderItem): Flow<ApiResult<List<OrderItem>>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                val newOrders = supabaseClient.postgrest.from("Order").insert(orderItem).decodeList<OrderItem>()
                emit(ApiResult.Success(newOrders))
            }
            catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }

        }
    }

    fun addNewOrderProductDetails(insertParam: InsertParam): Flow<ApiResult<Unit>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                supabaseClient.postgrest.from("Order_Product").insert(insertParam)
                emit(ApiResult.Success(Unit))
            }
            catch (e: Exception) {
                Log.e("OrderDataSource", e.message!!)
                emit(ApiResult.Error(e.message))
            }
        }
    }

    fun getRealtimeStatus(): Realtime.Status {
        return supabaseClient.realtime.status.value
    }

    suspend fun listenToChange(channelName: String ,tableName: String): Flow<PostgresAction> {
        val channel = supabaseClient.realtime.createChannel(channelName)
        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                table = tableName
        }

        supabaseClient.realtime.connect()
        channel.join()
        return changeFlow
    }


    fun getOrderDetailsByID(id: String): Flow<ApiResult<OrderDetailItem>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                val queryRes = supabaseClient.postgrest["Order"]
                    .select(columns = Columns.list(
                        "*",
                        "Product(*)",
                        "Order_Product(quantity)"
                    )) {
                        eq("id", id)
                    }

                val order = queryRes.decodeSingle<OrderDetailItem>()
                emit(ApiResult.Success(order))
            }
            catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }
        }
    }

    fun updateOrderStatus(id: String, newStatus: String): Flow<ApiResult<Unit>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                supabaseClient.postgrest["Order"].update({
                    set("status", newStatus)
                }) {
                    eq("id",id)
                }

                emit(ApiResult.Success(Unit))
            }
            catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }
        }
    }

    fun getAllProducts(): Flow<ApiResult<List<ProductItem>>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                val queryRes = supabaseClient.postgrest.from("Product")
                    .select(columns = Columns.list(
                        "*"
                    )) {
                    }

                val products = queryRes.decodeList<ProductItem>()
                println(products.toString())
                emit(ApiResult.Success(products))
            }
            catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }

        }
    }



}

