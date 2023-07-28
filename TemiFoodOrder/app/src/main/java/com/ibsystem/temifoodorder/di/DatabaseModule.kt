package com.ibsystem.temifoodorder.di

import android.content.Context
import com.ibsystem.temifoodorder.datasource.SupabaseDataSource
import com.ibsystem.temifoodorder.repository.SupabaseRepository
import com.ibsystem.temifoodorder.repository.SupabaseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseDatabase(
        @ApplicationContext context: Context
    ): SupabaseClient {
        return createSupabaseClient(
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJ5bGh2bnZ2Y3dvZnN1d3Bvem9oIiwicm9sZSI6ImFub24iLCJpYXQiOjE2OTA1MDQ3ODUsImV4cCI6MjAwNjA4MDc4NX0.Mg6Z6zXG42CxfScgxzBwzVOmk85-J8WEvUG9oK2d00o",
            supabaseUrl = "https://bylhvnvvcwofsuwpozoh.supabase.co"
        ) {
            install(Postgrest)
            install(Realtime)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseDataSource(
        database: SupabaseClient
    ): SupabaseDataSource {
        return SupabaseDataSource(database)
    }


    @Provides
    @Singleton
    fun provideSupabaseRepository(dataSource: SupabaseDataSource): SupabaseRepository {
        return SupabaseRepositoryImpl(dataSource)
    }

}