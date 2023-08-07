package com.ibsystem.temifoodorder.module

import com.ibsystem.temifoodorder.domain.model.TableModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InformationModule {
    @Provides
    @Singleton
    fun provideTableInfo(tableID: String): TableModel {
        return TableModel("1")
    }
}