package com.farmdirect.app.data.remote

import com.farmdirect.app.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("crops")
    suspend fun getCrops(@Header("Authorization") token: String, @Query("category") category: String? = null): Response<List<Crop>>

    @GET("crops/{id}")
    suspend fun getCrop(@Header("Authorization") token: String, @Path("id") cropId: String): Response<Crop>

    @POST("crops")
    suspend fun postCrop(@Header("Authorization") token: String, @Body request: PostCropRequest): Response<Crop>

    @GET("chats")
    suspend fun getChats(@Header("Authorization") token: String): Response<List<Chat>>

    @GET("chats/{chatId}/messages")
    suspend fun getMessages(@Header("Authorization") token: String, @Path("chatId") chatId: String): Response<List<Message>>

    @POST("messages")
    suspend fun sendMessage(@Header("Authorization") token: String, @Body request: SendMessageRequest): Response<Message>

    @POST("orders")
    suspend fun placeOrder(@Header("Authorization") token: String, @Body request: PlaceOrderRequest): Response<Order>

    @GET("orders")
    suspend fun getOrders(@Header("Authorization") token: String): Response<List<Order>>

    @POST("payments/mpesa")
    suspend fun initiateMpesaPayment(@Header("Authorization") token: String, @Body request: MpesaPaymentRequest): Response<Payment>

    @GET("livestock")
    suspend fun getLivestock(@Header("Authorization") token: String, @Query("animalType") animalType: String? = null): Response<List<Livestock>>

    @POST("livestock")
    suspend fun postLivestock(@Header("Authorization") token: String, @Body request: PostLivestockRequest): Response<Livestock>

    @GET("weather/forecast")
    suspend fun getWeather(@Header("Authorization") token: String, @Query("latitude") lat: Double, @Query("longitude") lng: Double): Response<WeatherForecast>

    @GET("finance/summary")
    suspend fun getFinanceSummary(@Header("Authorization") token: String): Response<FinanceSummary>
}
