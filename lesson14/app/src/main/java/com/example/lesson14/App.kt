package com.example.lesson14

import android.app.Application
import java.io.File

class App : Application() {
    private lateinit var pathTofile: String

    companion object {
        lateinit var INSTANCE: App
        private const val FILE_NAME = "elements content.txt"
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        createPathToFileFile()
    }

    private fun createPathToFileFile() {
        pathTofile = StringBuilder(filesDir.absolutePath)
            .append(File.separator)
            .append(FILE_NAME)
            .toString()
    }

    fun getPathFile(): String = pathTofile
}