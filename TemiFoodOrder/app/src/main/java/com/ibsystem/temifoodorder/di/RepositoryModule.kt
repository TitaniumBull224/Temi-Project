package com.ibsystem.temifoodorder.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.ibsystem.temifoodorder.data.repository.OnBoardingOperationImpl
import com.ibsystem.temifoodorder.data.repository.Repository
import com.ibsystem.temifoodorder.domain.repository.OnBoardingOperations
import com.ibsystem.temifoodorder.domain.usecase.UseCases
import com.ibsystem.temifoodorder.domain.usecase.addcartusecase.AddCartUseCase
import com.ibsystem.temifoodorder.domain.usecase.deletecartusecase.DeleteCartUseCase
import com.ibsystem.temifoodorder.domain.usecase.getallcartusecase.GetAllCartUseCase
import com.ibsystem.temifoodorder.domain.usecase.getallproduct.GetAllProductUseCase
import com.ibsystem.temifoodorder.domain.usecase.getselectedproduct.GetSelectedProductUseCase
import com.ibsystem.temifoodorder.domain.usecase.readonboarding.ReadOnBoardingUseCase
import com.ibsystem.temifoodorder.domain.usecase.saveonboarding.SaveOnBoardingUseCase
import com.ibsystem.temifoodorder.domain.usecase.saveproductusecase.InsertProductsUseCase
import com.ibsystem.temifoodorder.domain.usecase.searchproductusecase.SearchProductUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDataStoreOperation(
        @ApplicationContext context: Context
    ): OnBoardingOperations = OnBoardingOperationImpl(context = context)

    @Provides
    @Singleton
    fun provideUseCase(repository: Repository): UseCases {
        return UseCases(
            saveOnBoardingUseCase = SaveOnBoardingUseCase(repository),
            insertProductsUseCase = InsertProductsUseCase(repository),
            readOnBoardingUseCase = ReadOnBoardingUseCase(repository),
            getSelectedProductUseCase = GetSelectedProductUseCase(repository),
            getAllProductUseCase = GetAllProductUseCase(repository),
            getAllCartUseCase = GetAllCartUseCase(repository),
            addCartUseCase = AddCartUseCase(repository),
            deleteCart = DeleteCartUseCase(repository),
            searchProductUseCase = SearchProductUseCase(repository)
        )
    }

}