package com.ibsystem.temifoodorder.domain.usecase.saveproductusecase

import com.ibsystem.temifoodorder.data.repository.Repository
import com.ibsystem.temifoodorder.domain.model.ProductItem

class InsertProductsUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(products: List<ProductItem>) = repository.insertProducts(products)

}