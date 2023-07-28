package com.ibsystem.temifoodorder.domain.usecase.deletecartusecase

import com.ibsystem.temifoodorder.data.repository.Repository
import com.ibsystem.temifoodorder.domain.model.ProductItem

class DeleteCartUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(productItem: ProductItem) = repository.deleteCart(productItem)

}