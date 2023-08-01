package com.ibsystem.temiassistant.network

import com.ibsystem.temiassistant.domain.model.WeatherModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val apiKey = "254ad7f324af7a5dfbd99f056aec068b"

private var retrofit_weather: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.openweathermap.org/")
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .build()

var openWeatherApiService: OpenWeatherApiService = retrofit_weather.create(OpenWeatherApiService::class.java)

interface OpenWeatherApiService {
    @Headers("Content-Type: application/json")
    @GET("data/2.5/weather")
    suspend fun getWeatherData(@Query("lat") lat: String,
                               @Query("lon") lon: String,
                               @Query("lang") lang: String = "ja",
                               @Query("appid") appID: String = apiKey,
                               @Query("units") units: String = "metric"
    ): Response<WeatherModel>
}