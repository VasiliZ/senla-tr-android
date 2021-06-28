package com.github.rtyvz.senla.tr.myapplication

import android.app.Application
import com.github.rtyvz.senla.tr.myapplication.models.State
import com.github.rtyvz.senla.tr.myapplication.network.UserApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    var state: State? = null

    companion object {
        lateinit var INSTANCE: App
        lateinit var okHttpClient: OkHttpClient
        lateinit var api: UserApi
        private const val BASE_URL =
            "https://pub.zame-dev.org/senla-training-addition/"
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        okHttpClient = OkHttpClient.Builder()
            .build()
        api = provideApi()
    }

    private fun provideApi() = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)

        .addConverterFactory(
            GsonConverterFactory
                .create()
        )
        .build()
        .create(UserApi::class.java)
}