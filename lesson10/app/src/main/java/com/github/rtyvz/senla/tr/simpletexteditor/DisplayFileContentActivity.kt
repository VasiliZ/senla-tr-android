package com.github.rtyvz.senla.tr.simpletexteditor

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.simpletexteditor.databinding.DisplayFileActivityBinding
import java.io.File

class DisplayFileContentActivity : AppCompatActivity() {
    private lateinit var binding: DisplayFileActivityBinding
    private lateinit var prefs: SharedPreferences
    private var filePath: String = EMPTY_STRING

    companion object {
        private const val CHAR_SET = "UTF-8"
        private const val LINE_BREAK = "\n"
        private const val EMPTY_STRING = ""
        private const val DEFAULT_PREFS_INT_VALUE = 0
        private const val DEFAULT_PREFS_FLOAT_VALUE = 0F
        private const val ADDITION = 1
        private const val DOT = "."
        private const val SPACE = "\t"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DisplayFileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences(SettingsActivity.SETTING_PREFS, Context.MODE_PRIVATE)
        filePath = StringBuilder().append(filesDir.absolutePath).append(File.separator)
            .append(MainActivity.FILE_NAME).toString()
        val textColor = prefs.getInt(SettingsActivity.SAVE_COLOR_PREFS, DEFAULT_PREFS_INT_VALUE)
        val textSize =
            prefs.getFloat(SettingsActivity.SAVE_TEXT_SIZE_PREFS, DEFAULT_PREFS_FLOAT_VALUE)
        val contentFile = StringBuilder()

        writeDataFromFile().forEachIndexed { index, s ->
            contentFile
                .append(index + ADDITION)
                .append(DOT)
                .append(SPACE)
                .append(s)
                .append(LINE_BREAK)
        }

        makeViewSettings(textColor, textSize)

        binding.displayFileContentTextView.text = contentFile
    }

    private fun writeDataFromFile() = File(filePath).bufferedReader(charset(CHAR_SET)).readLines()

    private fun makeViewSettings(textColor: Int, textSize: Float) {

        if (textColor != DEFAULT_PREFS_INT_VALUE) binding.displayFileContentTextView.setTextColor(
            textColor
        )

        if (textSize != DEFAULT_PREFS_FLOAT_VALUE)
            binding.displayFileContentTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                textSize
            )
    }
}