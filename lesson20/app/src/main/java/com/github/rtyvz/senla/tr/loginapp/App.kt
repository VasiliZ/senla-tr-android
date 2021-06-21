package com.github.rtyvz.senla.tr.loginapp

import android.app.Application
import java.io.File

class App : Application() {
    private lateinit var userProfileFile: File

    companion object {
        lateinit var INSTANCE: App
        private const val USER_PROFILE_FILE = "user.txt"
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        createUserDataFile()
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