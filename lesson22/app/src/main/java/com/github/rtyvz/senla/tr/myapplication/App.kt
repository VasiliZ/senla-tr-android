package com.github.rtyvz.senla.tr.myapplication

import android.app.Application
import com.github.rtyvz.senla.tr.myapplication.models.State
import com.github.rtyvz.senla.tr.myapplication.network.UserApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    private val state: State = State()
    private lateinit var api: UserApi

    companion object {
        lateinit var INSTANCE: App
        private const val BASE_URL = "https://pub.zame-dev.org/senla-training-addition/"
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        initRetrofit()
    }

    fun getState() = state

    fun getApi() = api

    private fun initOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(loggingInterceptor)
        }

        return httpClient.build()
    }

    private fun initRetrofit() {
        api = Retrofit.Builder()
            .client(initOkHttpClient())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }
}