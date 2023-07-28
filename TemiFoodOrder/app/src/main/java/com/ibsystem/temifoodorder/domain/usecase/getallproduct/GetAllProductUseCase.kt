package com.ibsystem.temifoodorder.domain.usecase.getallproduct

import com.ibsystem.temifoodorder.data.repository.Repository
import com.ibsystem.temifoodorder.domain.model.ProductItem
import kotlinx.coroutines.flow.Flow

class GetAllProductUseCase(
    private val repository: Repository
) {

    operator fun invoke(): Flow<List<ProductItem>> = repository.getAllProduct()

}