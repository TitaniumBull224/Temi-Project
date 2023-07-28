package com.ibsystem.temifoodorder.domain.usecase.getselectedproduct

import com.ibsystem.temifoodorder.data.repository.Repository
import com.ibsystem.temifoodorder.domain.model.ProductItem

class GetSelectedProductUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(productId: Int): ProductItem {
        return repository.getSelectedProduct(productId = productId)
    }

}