package com.github.rtyvz.senla.tr.notebook

import android.app.Application
import java.io.File

class NotebookApp : Application() {

    companion object {
        private var instance: NotebookApp? = null
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