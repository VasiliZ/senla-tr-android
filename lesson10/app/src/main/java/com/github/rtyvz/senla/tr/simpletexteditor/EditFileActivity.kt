package com.github.rtyvz.senla.tr.simpletexteditor

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.simpletexteditor.databinding.EditFileActivityBinding
import java.io.File

class EditFileActivity : AppCompatActivity() {
    private lateinit var binding: EditFileActivityBinding
    private lateinit var prefs: SharedPreferences
    private var filePath: String = EMPTY_STRING

    companion object {
        private const val CHAR_SET = "UTF-8"
        private const val LINE_BREAK = "\n"
        private const val EMPTY_STRING = ""
        private const val DEFAULT_PREFS_INT_VALUE = 0
        private const val DEFAULT_PREFS_FLOAT_VALUE = 0F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = EditFileActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        filePath = StringBuilder().append(filesDir.absolutePath).append(File.separator)
            .append(MainActivity.FILE_NAME).toString()
        prefs = getSharedPreferences(SettingsActivity.SETTING_PREFS, Context.MODE_PRIVATE)
        val textColor = prefs.getInt(
            SettingsActivity.SAVE_COLOR_PREFS,
            DEFAULT_PREFS_INT_VALUE
        )
        val textSize =
            prefs.getFloat(
                SettingsActivity.SAVE_TEXT_SIZE_PREFS,
                DEFAULT_PREFS_FLOAT_VALUE
            )
        makeViewSettings(textColor, textSize)

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

    private fun makeViewSettings(textColor: Int, textSize: Float) {

        if (textColor != DEFAULT_PREFS_INT_VALUE) {
            binding.editFileContentEditText.setTextColor(
                textColor
            )
        }

        if (textSize != DEFAULT_PREFS_FLOAT_VALUE) {
            binding.editFileContentEditText.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                textSize
            )
        }
    }
}