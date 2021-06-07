package com.github.rtyvz.senla.tr.notebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.notebook.databinding.EditFileActivityBinding
import java.io.File

class EditFileActivity : AppCompatActivity() {
    private lateinit var binding: EditFileActivityBinding
    private var savedFilePath: String? = null

    companion object {
        private const val CHAR_SET = "UTF-8"
        private const val EMPTY_STRING = ""
        private const val MAX_FILE_NAME_SIZE = 30
        private const val START_STRING_INDEX = 0
        private const val FILE_EXT = ".txt"
        private const val LINE_BREAK = "\n"
        const val PATH_FILE_EXTRA = "PATH_FILE_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditFileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            savedFilePath = it.getStringExtra(PATH_FILE_EXTRA) ?: EMPTY_STRING
        }
        val contentFileBuilder = StringBuilder()

        savedFilePath?.let {
            readFromFile(it).forEach {
                contentFileBuilder.append(it).append(LINE_BREAK)
            }
            binding.editFileEditText.setText(contentFileBuilder.toString())
        }
    }

    private fun writeToFile(value: String) {

        if (value.isNotBlank()) {
            val fileName = createFileName(value)
            savedFilePath?.let {
                val fileForOperations = File(it)
                val dirToFile = fileForOperations.parent
                val oldFileName = fileForOperations.name
                val newFileNameBuilder =
                    StringBuilder(fileName)
                        .append(FILE_EXT).toString()
                File(dirToFile, oldFileName).renameTo(File(dirToFile, newFileNameBuilder))
                val newPathBuilder =
                    StringBuilder(dirToFile ?: EMPTY_STRING).append(File.separator)
                        .append(newFileNameBuilder)
                File(newPathBuilder.toString()).bufferedWriter(charset = charset(CHAR_SET)).use {
                    it.write(value)
                }
            } ?: run {
                val filePathBuilder = buildPathForNewFile(fileName)
                File(filePathBuilder).createNewFile()
                File(filePathBuilder).bufferedWriter(charset = charset(CHAR_SET)).use {
                    it.write(value)
                }
            }
        }
    }

    override fun onPause() {
        writeToFile(binding.editFileEditText.text.toString())

        super.onPause()

    }

    private fun readFromFile(filePath: String) = File(filePath).bufferedReader().readLines()

    private fun buildPathForNewFile(fileName: String): String {
        return StringBuilder()
            .append(NotebookApp.INSTANCE?.getNotebookDirPath())
            .append(File.separator)
            .append(fileName)
            .append(FILE_EXT).toString()
    }

    private fun createFileName(value: String): String {
        return when {
            value.contains(LINE_BREAK) ->
                if (value.length <= MAX_FILE_NAME_SIZE
                    || value.indexOf(LINE_BREAK) < MAX_FILE_NAME_SIZE
                ) {
                    value.substring(START_STRING_INDEX, value.indexOf(LINE_BREAK)).trim()
                } else {
                    value.substring(START_STRING_INDEX, MAX_FILE_NAME_SIZE).trim()
                }
            value.length <= MAX_FILE_NAME_SIZE -> value.trim()
            value.length >= MAX_FILE_NAME_SIZE -> value
                .substring(START_STRING_INDEX, MAX_FILE_NAME_SIZE).trim()
            else -> {
                EMPTY_STRING
            }
        }
    }
}