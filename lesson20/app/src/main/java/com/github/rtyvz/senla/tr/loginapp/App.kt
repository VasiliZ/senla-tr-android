package com.github.rtyvz.senla.tr.loginapp

import android.app.Application
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.loginapp.task.GetUserProfileTask
import com.github.rtyvz.senla.tr.loginapp.task.LoginTask
import okhttp3.OkHttpClient

class App : Application() {
    var state = State()

    companion object {
        lateinit var INSTANCE: App
        private lateinit var httpClient: OkHttpClient
        private lateinit var localBroadcastManager: LocalBroadcastManager
    }

    object TasksProvider {
        fun provideLoginTask() = LoginTask(httpClient, INSTANCE, localBroadcastManager)
        fun provideFetchProfileTask() = GetUserProfileTask(httpClient, localBroadcastManager)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        httpClient = OkHttpClient.Builder().build()
    }
}