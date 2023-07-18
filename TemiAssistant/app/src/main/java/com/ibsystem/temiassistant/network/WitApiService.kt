package com.ibsystem.temiassistant.network

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ibsystem.temiassistant.presentation.chat_ui.MessageBody
import com.ibsystem.temiassistant.presentation.chat_ui.ResponseMessage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.IOException


private var token = "OLB4Y4VHT3AHLE43EVCSNDCCGVCZT4N5"

private var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(
    Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(newRequest)
    },
).build()

val moshi: Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()


private var retrofit: Retrofit = Retrofit.Builder()
    .client(client)
    .baseUrl("https://api.wit.ai/")
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .build()

var witApiService: WitApiService = retrofit.create(WitApiService::class.java)

interface WitApiService {
    @Headers("Content-Type: application/json")
    @POST("/event")
    suspend fun sendMessage(@Query("session_id") session_id: String = "test1",
                            @Body message: MessageBody
    ): Response<ResponseMessage>
}