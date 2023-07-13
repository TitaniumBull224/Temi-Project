package com.ibsystem.temiassistant.network

<<<<<<< Updated upstream
<<<<<<< HEAD
//import retrofit2.http.POST

//interface WitApiService {
//    @POST()
//}
=======
=======
import android.os.Bundle
>>>>>>> Stashed changes
import androidx.compose.runtime.snapshots.SnapshotStateList
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.IOException


private var token = "LB4Y4VHT3AHLE43EVCSNDCCGVCZT4N5"

private var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
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

var witApiService = retrofit.create(WitApiService::class.java)

interface WitApiService {
    @POST("/event")
<<<<<<< Updated upstream
    suspend fun sendMessage(
        @Body message: Message
    ): Response<SnapshotStateList<Message>>
}
>>>>>>> 080b3ee47a35dbf9aed3b7ee576b76deb2d5893d
=======
    suspend fun sendMessage(@Query("session_id") session_id: String = "test1",
                            @Body message: MessageBody
    ): Response<SnapshotStateList<ResponseMessage>>
}

/*

private lateinit var httpClient: OkHttpClient
private lateinit var httpBuilder: HttpUrl.Builder
private lateinit var httpRequestBuilder: Request.Builder

/* Go to your Wit.ai app Management > Settings and obtain the Client Access Token */
private const val CLIENT_ACCESS_TOKEN = "<YOUR CLIENT ACCESS TOKEN>"


/*
   * ADD the following function to initialize OkHttp for streaming to the Speech API
 */
private fun initializeHttpClient() {
    httpClient = OkHttpClient()
    httpBuilder = "https://api.wit.ai/speech".toHttpUrlOrNull()!!.newBuilder()
    httpBuilder.addQueryParameter("v", "20200805")
    httpRequestBuilder = Request.Builder()
        .url(httpBuilder.build())
        .header("Authorization", "Bearer $CLIENT_ACCESS_TOKEN")
        .header("Content-Type", "audio/raw")
        .header("Transfer-Encoding", "chunked")
}*/
>>>>>>> Stashed changes
