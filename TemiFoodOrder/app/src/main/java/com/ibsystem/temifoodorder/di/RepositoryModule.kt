//package com.ibsystem.temifoodorder.di
//
//import android.content.Context
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object RepositoryModule {
//
//    @Provides
//    @Singleton
//    fun provideDataStoreOperation(
//        @ApplicationContext context: Context
//    ): OnBoardingOperations = OnBoardingOperationImpl(context = context)
//
//    @Provides
//    @Singleton
//    fun provideUseCase(repository: Repository): UseCases {
//        return UseCases(
//            saveOnBoardingUseCase = SaveOnBoardingUseCase(repository),
//            insertProductsUseCase = InsertProductsUseCase(repository),
//            readOnBoardingUseCase = ReadOnBoardingUseCase(repository),
//            getSelectedProductUseCase = GetSelectedProductUseCase(repository),
//            getAllProductUseCase = GetAllProductUseCase(repository),
//            getAllCartUseCase = GetAllCartUseCase(repository),
//            addCartUseCase = AddCartUseCase(repository),
//            deleteCart = DeleteCartUseCase(repository),
//            searchProductUseCase = SearchProductUseCase(repository)
//        )
//    }
//
//}