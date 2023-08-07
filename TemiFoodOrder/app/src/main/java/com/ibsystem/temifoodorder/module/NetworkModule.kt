package com.ibsystem.temifoodorder.module
//
//import com.ibsystem.temifoodorder.data.datasource.OrderDataSource
//import com.ibsystem.temifoodorder.data.repository.OrderRepository
//import com.ibsystem.temifoodorder.data.repository.OrderRepositoryImpl
import com.ibsystem.temifooddelivery.data.datasource.OrderDataSource
import com.ibsystem.temifooddelivery.data.repository.OrderRepository
import com.ibsystem.temifooddelivery.data.repository.OrderRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://bylhvnvvcwofsuwpozoh.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJ5bGh2bnZ2Y3dvZnN1d3Bvem9oIiwicm9sZSI6ImFub24iLCJpYXQiOjE2OTA1MDQ3ODUsImV4cCI6MjAwNjA4MDc4NX0.Mg6Z6zXG42CxfScgxzBwzVOmk85-J8WEvUG9oK2d00o"
        ) {
            install(Postgrest)
            install(Realtime)
        }
    }

    @Provides
    @Singleton
    fun provideOrderDataSource(client: SupabaseClient): OrderDataSource {
        return OrderDataSource(client)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(dataSource: OrderDataSource): OrderRepository {
        return OrderRepositoryImpl(dataSource)
    }
}