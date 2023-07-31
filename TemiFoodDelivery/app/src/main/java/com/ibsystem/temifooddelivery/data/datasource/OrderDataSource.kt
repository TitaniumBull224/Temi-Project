package com.ibsystem.temifooddelivery.data.datasource

import androidx.compose.ui.text.android.style.BaselineShiftSpan
import com.ibsystem.temifooddelivery.domain.OrderModel
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrderDataSource @Inject constructor(private val supabaseClient: SupabaseClient) {
     fun getAllOrders(): Flow<ApiResult<List<OrderModelItem>>> {
         return flow {
             emit(ApiResult.Loading)
             try {
                 val queryRes = supabaseClient.postgrest.from("Order")
                     .select(columns = Columns.raw(
                         """
                    *,
                    Product(*),
                    Order_Product(quantity)
                """.trimIndent()
                     ))
                 val orders = queryRes.decodeList<OrderModelItem>()
                 emit(ApiResult.Success(orders))
             }
             catch (e: Exception) {
                emit(ApiResult.Error(e.message))
             }

         }


    }
}

sealed class ApiResult<out R> {
    data class Success<out R>(val data: R): ApiResult<R>()
    data class Error(val message: String?): ApiResult<Nothing>()
    object Loading: ApiResult<Nothing>()
}