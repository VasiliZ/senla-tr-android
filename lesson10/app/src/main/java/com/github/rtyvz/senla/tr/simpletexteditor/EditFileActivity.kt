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
        private const val DOT = "."
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = EditFileActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        filePath = StringBuilder().append(filesDir.absolutePath).append(File.separator)
            .append(MainActivity.FILE_NAME).toString()
        prefs = getSharedPreferences(SettingsActivity.PREFS_SETTING, Context.MODE_PRIVATE)
        val textColor = prefs.getString(
            SettingsActivity.PREFS_COLOR,
            EMPTY_STRING
        )
        val textSize =
            prefs.getString(
                SettingsActivity.PREFS_TEXT_SIZE,
                EMPTY_STRING
            )
        makeViewSettings(textColor, textSize)

        if (!File(filePath).exists()) {
            File(filePath).createNewFile()
        } else {
            val tempValueForWriteBackToFile = StringBuilder()
            readFromFile().forEach {
                tempValueForWriteBackToFile.append(it).append(LINE_BREAK)
            }
            binding.editFileContentEditText.setText(
                tempValueForWriteBackToFile.toString()
            )
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

    private fun makeViewSettings(textColor: String?, textSize: String?) {
        binding.apply {
            textColor?.let {
                when (it) {
                    Color.BLACK.name -> editFileContentEditText.setTextColor(
                        resources.getColor(
                            R.color.black,
                            null
                        )
                    )
                    Color.RED.name -> editFileContentEditText.setTextColor(
                        resources.getColor(
                            R.color.red,
                            null
                        )
                    )
                    else -> editFileContentEditText.setTextColor(
                        resources.getColor(
                            R.color.green,
                            null
                        )
                    )
                }
            }

            textSize?.let {
                when (it) {
                    TextSize.SMALL.name -> {
                        editFileContentEditText.setTextSize(
                            TypedValue.COMPLEX_UNIT_SP,
                            resources.getDimension(R.dimen.small_text_size)
                        )
                    }
                    TextSize.MIDDLE.name -> {
                        editFileContentEditText.setTextSize(
                            TypedValue.COMPLEX_UNIT_SP,
                            resources.getDimension(R.dimen.middle_text_size)
                        )
                    }
                    else -> {
                        editFileContentEditText.setTextSize(
                            TypedValue.COMPLEX_UNIT_SP,
                            resources.getDimension(R.dimen.large_text_size)
                        )
                    }
                }
            }
        }
    }
}