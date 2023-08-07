package com.ibsystem.temifooddelivery.data.datasource

import android.util.Log
import com.ibsystem.temifoodorder.domain.model.OrderModelItem
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.createChannel
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderDataSource @Inject constructor(private val supabaseClient: SupabaseClient) {
     fun getAllOrders(tableID: String): Flow<ApiResult<List<OrderModelItem>>> {
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

                 val orders = queryRes.decodeList<OrderModelItem>()
//                 println(orders.toString())
                 emit(ApiResult.Success(orders))
             }
             catch (e: Exception) {
                emit(ApiResult.Error(e.message))
             }

         }
    }

    suspend fun listenToOrdersChange(): Flow<PostgresAction> {
                val channel = supabaseClient.realtime.createChannel("OrderChanged")
                val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                    table = "Order"
                }
                supabaseClient.realtime.connect()
                channel.join()

                return changeFlow
    }


    fun getOrderDetailsByID(id: String): Flow<ApiResult<OrderModelItem>> {
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

                val order = queryRes.decodeSingle<OrderModelItem>()
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
                Log.i("UpdatePLS","123446454775675")
            }
            catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }
        }
    }

//    suspend fun listenToOrdersChange() {
//        coroutineScope {
//            kotlin.runCatching {
//                supabaseClient.realtime.connect()
//
//                supabaseClient.postgresChangeFlow<PostgresAction>("public") {
//                    table = "messages"
//                }.onEach {
//                    when(it) {
//                        is PostgresAction.Delete -> messages.value = messages.value.filter { message -> message.id != it.oldRecord["id"]!!.jsonPrimitive.int }
//                        is PostgresAction.Insert -> messages.value = messages.value + it.decodeRecord<Message>()
//                        is PostgresAction.Select -> error("Select should not be possible")
//                        is PostgresAction.Update -> error("Update should not be possible")
//                    }
//                }
//
//                realtimeChannel.join()
//
//            }.onFailure {
//                it.printStackTrace()
//            }
//        }
//
//        val channel = supabaseClient.realtime.createChannel("OrderChanged")
//        val changeFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
//            table = "Orders"
//        }
//
//        //in a new coroutine (or use Flow.onEach().launchIn(scope)):
//        changeFlow.collect {
//
//        }
//
//        supabaseClient.realtime.connect()
//        channel.join()
//    }
//}


}

sealed class ApiResult<out R> {
    data class Success<out R>(val data: R): ApiResult<R>()
    data class Error(val message: String?): ApiResult<Nothing>()
    object Loading: ApiResult<Nothing>()
}