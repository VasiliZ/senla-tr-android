package com.github.rtyvz.senla.tr.loginapp

import android.app.Application
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.loginapp.task.GetUserProfileTask
import com.github.rtyvz.senla.tr.loginapp.task.LoginTask
import okhttp3.OkHttpClient
import java.io.File

class App : Application() {
    private lateinit var userProfileFile: File
    var state = State()

    companion object {
        lateinit var INSTANCE: App
        private lateinit var httpClient: OkHttpClient
        private lateinit var localBroadcastManager: LocalBroadcastManager
        private const val USER_PROFILE_FILE = "user.txt"
    }

    object TasksProvider {
        fun provideLoginTask() = LoginTask(httpClient, INSTANCE, localBroadcastManager)
        fun provideFetchProfileTask() = GetUserProfileTask(httpClient, localBroadcastManager)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        createUserDataFile()
        httpClient = OkHttpClient.Builder().build()
    }

    private fun createUserDataFile() {
        val filePath =
            StringBuilder(filesDir.path).append(File.separator).append(USER_PROFILE_FILE).toString()
        val file = File(filePath)

        if (!file.exists()) {
            File(filePath).createNewFile()
        }
        userProfileFile = file
    }

    fun getUserInformationFile() = userProfileFile
}