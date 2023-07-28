package com.ibsystem.temifoodorder.domain.usecase.searchproductusecase

import com.ibsystem.temifoodorder.data.repository.Repository

class SearchProductUseCase(
    private val repository: Repository
) {

    operator fun invoke(query: String) = repository.searchProduct(query)

}