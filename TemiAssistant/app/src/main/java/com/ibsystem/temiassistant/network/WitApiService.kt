package com.ibsystem.temiassistant.network

import androidx.compose.runtime.snapshots.SnapshotStateList
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.IOException

private var token = "LB4Y4VHT3AHLE43EVCSNDCCGVCZT4N5"

private var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}).build()

private var retrofit: Retrofit = Retrofit.Builder()
    .client(client)
    .baseUrl("https://api.wit.ai/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface WitApiService {
    @POST("/event")
    suspend fun sendMessage(
        @Body message: Message
    ): Response<SnapshotStateList<Message>>
}