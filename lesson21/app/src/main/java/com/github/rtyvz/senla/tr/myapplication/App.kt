package com.github.rtyvz.senla.tr.myapplication

import android.app.Application
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.myapplication.common.BoltsFragment
import com.github.rtyvz.senla.tr.myapplication.models.State
import com.github.rtyvz.senla.tr.myapplication.models.TokenResponse
import com.github.rtyvz.senla.tr.myapplication.models.UserCredentials
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.ui.login.LoginActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class App : Application() {
    private val state: State = State()

    companion object {
        lateinit var INSTANCE: App
        lateinit var okHttpClient: OkHttpClient
    }

    object TaskProvider {
        fun getTokenTask() = INSTANCE.initTokenTask(okHttpClient)
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        okHttpClient = OkHttpClient.Builder().build()
    }

    fun getState() = state


}