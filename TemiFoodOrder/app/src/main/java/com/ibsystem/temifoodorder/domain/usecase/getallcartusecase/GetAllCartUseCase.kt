package com.ibsystem.temifoodorder.domain.usecase.getallcartusecase

import com.ibsystem.temifoodorder.data.repository.Repository
import com.ibsystem.temifoodorder.domain.model.ProductItem
import kotlinx.coroutines.flow.Flow

class GetAllCartUseCase(
    private val repository: Repository
) {

    operator fun invoke(isCart: Boolean): Flow<List<ProductItem>> =
        repository.getAllProductCart(isCart)

}