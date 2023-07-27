package com.ibsystem.temiassistant.network

import com.ibsystem.temiassistant.presentation.chat_ui.ConvertResponse
import com.ibsystem.temiassistant.presentation.chat_ui.WeatherModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val apiKeyCurrency = "yp3IHXQkOMHM1YdgIweKb5jYXrNZNyB8"

private var retrofit_currency: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.currencybeacon.com/v1/")
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .build()

var currencyApiService: CurrencyApiService = retrofit_currency.create(CurrencyApiService::class.java)

interface CurrencyApiService {
    @Headers("Content-Type: application/json")
    @GET("convert")
    suspend fun convertCurrency(@Query("from") from: String,
                               @Query("to") to: String,
                               @Query("amount") amount: String,
    ): Response<ConvertResponse>
}