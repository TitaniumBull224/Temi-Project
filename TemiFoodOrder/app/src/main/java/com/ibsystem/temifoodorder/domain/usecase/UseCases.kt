package com.ibsystem.temifoodorder.domain.usecase

import com.ibsystem.temifoodorder.domain.usecase.addcartusecase.AddCartUseCase
import com.ibsystem.temifoodorder.domain.usecase.deletecartusecase.DeleteCartUseCase
import com.ibsystem.temifoodorder.domain.usecase.getallcartusecase.GetAllCartUseCase
import com.ibsystem.temifoodorder.domain.usecase.getallproduct.GetAllProductUseCase
import com.ibsystem.temifoodorder.domain.usecase.getselectedproduct.GetSelectedProductUseCase
import com.ibsystem.temifoodorder.domain.usecase.readonboarding.ReadOnBoardingUseCase
import com.ibsystem.temifoodorder.domain.usecase.saveonboarding.SaveOnBoardingUseCase
import com.ibsystem.temifoodorder.domain.usecase.saveproductusecase.InsertProductsUseCase
import com.ibsystem.temifoodorder.domain.usecase.searchproductusecase.SearchProductUseCase

data class UseCases(
    val saveOnBoardingUseCase: SaveOnBoardingUseCase,
    val insertProductsUseCase: InsertProductsUseCase,
    val readOnBoardingUseCase: ReadOnBoardingUseCase,
    val getSelectedProductUseCase: GetSelectedProductUseCase,
    val getAllProductUseCase: GetAllProductUseCase,
    val getAllCartUseCase: GetAllCartUseCase,
    val addCartUseCase: AddCartUseCase,
    val deleteCart: DeleteCartUseCase,
    val searchProductUseCase: SearchProductUseCase
)