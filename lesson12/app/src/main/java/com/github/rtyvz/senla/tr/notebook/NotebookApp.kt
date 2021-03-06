package com.github.rtyvz.senla.tr.notebook

import android.app.Application
import java.io.File

class NotebookApp : Application() {
    private lateinit var noteBookDir: File

    companion object {
        var INSTANCE: NotebookApp? = null
        private const val NOTEBOOK_DIR_NAME = "documents"
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        noteBookDir = File(
            StringBuilder()
                .append(filesDir)
                .append(File.separator)
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

    fun getNotebookDir(): String = noteBookDir.absolutePath
}