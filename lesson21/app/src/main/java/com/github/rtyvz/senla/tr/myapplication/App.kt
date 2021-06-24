package com.github.rtyvz.senla.tr.myapplication

import android.app.Application
import com.github.rtyvz.senla.tr.myapplication.models.State
import com.github.rtyvz.senla.tr.myapplication.providers.ProfileProvider
import com.github.rtyvz.senla.tr.myapplication.providers.TokenProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient

class App : Application() {
    private val state: State = State()

    companion object {
        lateinit var INSTANCE: App
        lateinit var okHttpClient: OkHttpClient
        lateinit var gson: Gson
    }

    object TaskProvider {
        fun getTokenTask() = TokenProvider(okHttpClient, INSTANCE, gson)
        fun getProfileTask() = ProfileProvider(okHttpClient, gson, INSTANCE)
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        okHttpClient = OkHttpClient.Builder().build()
        gson = GsonBuilder().create()
    }

    fun getState() = state


}