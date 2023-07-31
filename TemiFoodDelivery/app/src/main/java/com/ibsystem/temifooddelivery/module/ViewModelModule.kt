package com.ibsystem.temifooddelivery.module

import com.ibsystem.temifooddelivery.OrderViewModel
import com.ibsystem.temifooddelivery.data.datasource.OrderDataSource
import com.ibsystem.temifooddelivery.data.repository.OrderRepository
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