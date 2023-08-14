package com.ibsystem.temifoodorder.module

import com.ibsystem.temifooddelivery.data.repository.OrderRepository
import com.ibsystem.temifoodorder.data.repository.ProductRepository
import com.ibsystem.temifoodorder.domain.model.TableModel
import com.ibsystem.temifoodorder.presentation.screen.home.HomeViewModel
import com.ibsystem.temifoodorder.presentation.screen.order.OrderViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ViewModelModule {
    @Provides
    fun provideOrderViewModel(repository: OrderRepository, tableModel: TableModel): OrderViewModel {
        return OrderViewModel(repository, tableModel)
    }

//    @Provides
//    fun provideCartViewModel(repository: OrderRepository, tableModel: TableModel): com.ibsystem.temifoodorder.presentation.screen.cart.OrderViewModel {
//        return com.ibsystem.temifoodorder.presentation.screen.cart.OrderViewModel(repository, tableModel)
//    }

    @Provides
    fun provideHomeViewModel(productRepository: ProductRepository): HomeViewModel {
        return HomeViewModel(productRepository)
    }
}