package com.ibsystem.temifoodorder.datasource

import com.ibsystem.temifoodorder.domain.model.CategoryItem
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SupabaseDataSource @Inject constructor(
    private val supabaseClient: SupabaseClient
){
    fun findAllCategories(): Flow<ApiResult<List<CategoryItem>>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                val queryRes = supabaseClient.postgrest["Category"].select()
                val categories = queryRes.decodeList<CategoryItem>()
                emit(ApiResult.Success(categories))
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