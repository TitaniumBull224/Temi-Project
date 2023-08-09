package com.ibsystem.temiassistant.module

import com.ibsystem.temiassistant.presentation.screen.order_list.OrderViewModel
import com.ibsystem.temiassistant.data.repository.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ViewModelModule {
    @Provides
    fun provideOrderViewModel(repository: OrderRepository): OrderViewModel {
        return OrderViewModel(repository)
    }

}