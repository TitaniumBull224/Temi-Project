package com.ibsystem.temifoodorder.repository

import com.ibsystem.temifoodorder.datasource.ApiResult
import com.ibsystem.temifoodorder.datasource.SupabaseDataSource
import com.ibsystem.temifoodorder.domain.model.CategoryItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SupabaseRepository {
    fun findAllCategories(): Flow<ApiResult<List<CategoryItem>>>
}

class SupabaseRepositoryImpl @Inject constructor(
    private val dataSource: SupabaseDataSource
): SupabaseRepository {
    override fun findAllCategories(): Flow<ApiResult<List<CategoryItem>>> {
        return dataSource.findAllCategories()
    }

}