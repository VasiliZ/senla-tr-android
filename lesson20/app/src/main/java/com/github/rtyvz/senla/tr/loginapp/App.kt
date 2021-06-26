package com.github.rtyvz.senla.tr.loginapp

import android.app.Application
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import okhttp3.OkHttpClient

class App : Application() {
    var state: State? = null

    companion object {
        lateinit var INSTANCE: App
        lateinit var httpClient: OkHttpClient
        lateinit var localBroadcastManager: LocalBroadcastManager
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        httpClient = OkHttpClient.Builder().build()
        val x = listOf(
            listOf(1, 2, 2, 3),
            listOf(1, 2, 2, 3)
        )
        for (i in 0..x.size) {
            for (j in 0..i) {
                if (i == j) {

                }
            }
        }
    }
}