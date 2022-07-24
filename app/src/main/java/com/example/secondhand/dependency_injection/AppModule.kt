package com.example.secondhand.dependency_injection

import android.content.Context
import com.example.secondhand.network.ApiServices
import com.example.secondhand.roomdatabase.BuyerProductDao
import com.example.secondhand.roomdatabase.BuyerProductDatabase
import com.example.secondhand.roomdatabase.NotificationDao
import com.example.secondhand.roomdatabase.NotificationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://market-final-project.herokuapp.com/"

    private val logging: HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()

    @Provides
    @Singleton
    fun provideRetrofitForSeconHandApi(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideApiServices(retrofit: Retrofit): ApiServices =
        retrofit.create(ApiServices::class.java)

    @Provides
    @Singleton
    fun provideBuyerProductRoomDatabas(@ApplicationContext context: Context): BuyerProductDatabase =
        BuyerProductDatabase.getInstance(context)!!

    @Provides
    @Singleton
    fun provideBuyerProductListDao(buyerProductDatabase: BuyerProductDatabase): BuyerProductDao =
        buyerProductDatabase.buyerProductDao()

    @Provides
    @Singleton
    fun provideNotificationRoomDatabase(@ApplicationContext context: Context): NotificationDatabase =
        NotificationDatabase.getInstance(context)!!

    @Provides
    @Singleton
    fun provideNotificationDao(notificationDatabase: NotificationDatabase): NotificationDao =
        notificationDatabase.notificationDao()
}