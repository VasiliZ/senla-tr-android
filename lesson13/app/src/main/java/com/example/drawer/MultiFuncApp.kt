package com.example.drawer

import android.app.Application
import java.io.File

class MultiFuncApp : Application() {
    companion object {
        private var instance: MultiFuncApp? = null
        private const val NOTEBOOK_DIR_NAME = "documents"
        private val pathToDirBuilder = StringBuilder()
        private lateinit var noteBookDir: File

        fun getNotebookPath(): String = noteBookDir.absolutePath
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        noteBookDir = File(
            pathToDirBuilder.append(instance?.filesDir).append(File.separator)
                .append(NOTEBOOK_DIR_NAME)
                .toString()
        )
        createDirectory()
    }

    private fun createDirectory() {
        if (!noteBookDir.exists()) {
            noteBookDir.mkdir()
        }
    }
}