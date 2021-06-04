package com.github.rtyvz.senla.tr.simpletexteditor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.simpletexteditor.databinding.EditFileActivityBinding
import java.io.File

class EditFileActivity : AppCompatActivity() {
    private lateinit var binding: EditFileActivityBinding
    private var filePath: String = EMPTY_STRING

    companion object {
        private const val CHAR_SET = "UTF-8"
        private const val LINE_BREAK = "\n"
        private const val EMPTY_STRING = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = EditFileActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        filePath = StringBuilder().append(filesDir.absolutePath).append(File.separator)
            .append(MainActivity.FILE_NAME).toString()

        if (!File(filePath).exists()) {
            File(filePath).createNewFile()
        } else {
            val tempValueForWriteBackToFile = StringBuilder()
            readFromFile().forEach {
                tempValueForWriteBackToFile.append(it).append(LINE_BREAK)
            }
            binding.editFileContentEditText.text =
                tempValueForWriteBackToFile.toString().toEditable()
        }
    }

    override fun onPause() {
        super.onPause()

        binding.apply {
            if (editFileContentEditText.text?.isNotBlank() ?: return) {
                writeToFile(editFileContentEditText.text.toString())
            }
        }
    }

    private fun writeToFile(value: String) {
        File(filePath).bufferedWriter(charset = charset(CHAR_SET)).use {
            it.write(value)
        }
    }

    private fun readFromFile(): List<String> {
        return File(filePath).bufferedReader().readLines()
    }
}