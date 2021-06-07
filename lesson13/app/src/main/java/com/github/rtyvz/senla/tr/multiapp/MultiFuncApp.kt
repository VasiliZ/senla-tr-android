package com.github.rtyvz.senla.tr.multiapp

import android.app.Application
import java.io.File

class MultiFuncApp : Application() {
    private lateinit var noteBookDir: File

    companion object {
        var INSTANCE: MultiFuncApp? = null
        private const val NOTEBOOK_DIR_NAME = "documents"
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        noteBookDir = File(
            StringBuilder()
                .append(INSTANCE?.filesDir)
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