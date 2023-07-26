package com.ibsystem.temiassistant.network

import com.ibsystem.temiassistant.presentation.chat_ui.NewsModel
import com.ibsystem.temiassistant.presentation.chat_ui.WeatherModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val apiKeyNews = "98b5b40bfb9140f3b474903cec23fcbb"

private var retrofit_news: Retrofit = Retrofit.Builder()
    .baseUrl("https://newsapi.org/")
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .build()

var newsApiService: OpenWeatherApiService = retrofit_news.create(OpenWeatherApiService::class.java)

interface NewsApiService {
    @Headers("Content-Type: application/json")
    @GET("v2/top-headlines")
    suspend fun getWeatherData(@Query("country") lang: String = "jp",
                               @Query("apiKey") appID: String = apiKeyNews,

    ): Response<NewsModel>
}