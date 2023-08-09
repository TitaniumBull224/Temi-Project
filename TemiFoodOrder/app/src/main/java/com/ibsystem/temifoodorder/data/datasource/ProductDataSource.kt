package com.ibsystem.temifoodorder.data.datasource


import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.utils.ApiResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductDataSource @Inject constructor(private val supabaseClient: SupabaseClient) {
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

//    suspend fun closeConnection() {
//        supabaseClient.close()
//    }
}