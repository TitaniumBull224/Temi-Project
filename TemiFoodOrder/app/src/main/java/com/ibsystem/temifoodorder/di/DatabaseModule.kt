package com.ibsystem.temifoodorder.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.ibsystem.temifoodorder.data.local.ProductDatabase
import com.ibsystem.temifoodorder.data.repository.LocalDataSourceImpl
import com.ibsystem.temifoodorder.domain.repository.LocalDataSource
import com.ibsystem.temifoodorder.utils.Constants.PRODUCT_DATABASE
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ProductDatabase {
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            PRODUCT_DATABASE
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        database: ProductDatabase
    ): LocalDataSource {
        return LocalDataSourceImpl(database)
    }

}