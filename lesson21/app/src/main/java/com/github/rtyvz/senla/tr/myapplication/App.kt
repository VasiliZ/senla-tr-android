package com.github.rtyvz.senla.tr.myapplication

import android.app.Application
import com.github.rtyvz.senla.tr.myapplication.models.State
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient

class App : Application() {
    var state: State? = null

    companion object {
        lateinit var INSTANCE: App
        lateinit var okHttpClient: OkHttpClient
        lateinit var gson: Gson
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        okHttpClient = OkHttpClient.Builder().build()
        gson = GsonBuilder().create()
    }
}