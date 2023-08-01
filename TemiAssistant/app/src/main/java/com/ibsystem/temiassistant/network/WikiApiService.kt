package com.ibsystem.temiassistant.network

import com.ibsystem.temiassistant.domain.model.WikiQuery
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

private var retrofit_wiki: Retrofit = Retrofit.Builder()
    .baseUrl("https://ja.wikipedia.org/api/rest_v1/page/summary/")
    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
    .build()

var wikiApiService: WikiApiService = retrofit_wiki.create(WikiApiService::class.java)

interface WikiApiService {
    @Headers("Content-Type: application/json")
    @GET("{query_keyword}")
    suspend fun getPageSummary(@Path("query_keyword") query_keyword: String): Response<WikiQuery>
}