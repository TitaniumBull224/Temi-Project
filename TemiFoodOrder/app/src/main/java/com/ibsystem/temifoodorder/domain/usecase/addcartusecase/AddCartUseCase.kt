package com.ibsystem.temifoodorder.domain.usecase.addcartusecase

import com.ibsystem.temifoodorder.data.repository.Repository
import com.ibsystem.temifoodorder.domain.model.ProductItem

class AddCartUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(productItem: ProductItem) = repository.addCart(productItem)

}