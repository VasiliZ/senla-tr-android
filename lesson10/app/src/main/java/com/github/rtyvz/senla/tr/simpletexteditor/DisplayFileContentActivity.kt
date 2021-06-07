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
        private const val ADDITION = 1
        private const val DOT = "."
        private const val SPACE = "\t"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DisplayFileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences(SettingsActivity.PREFS_SETTING, Context.MODE_PRIVATE)
        filePath = StringBuilder().append(filesDir.absolutePath).append(File.separator)
            .append(MainActivity.FILE_NAME).toString()
        val textColor = prefs.getString(SettingsActivity.PREFS_COLOR, EMPTY_STRING)
        val textSize =
            prefs.getString(SettingsActivity.PREFS_TEXT_SIZE, EMPTY_STRING)
        val contentFile = StringBuilder()

        readDataFromFile().forEachIndexed { index, s ->
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

    private fun readDataFromFile() = File(filePath).bufferedReader(charset(CHAR_SET)).readLines()

    private fun makeViewSettings(textColor: String?, textSize: String?) {
        binding.apply {
            textColor?.let {
                when (it) {
                    Color.BLACK.name -> displayFileContentTextView.setTextColor(
                        resources.getColor(
                            R.color.black,
                            null
                        )
                    )
                    Color.RED.name -> displayFileContentTextView.setTextColor(
                        resources.getColor(
                            R.color.red,
                            null
                        )
                    )
                    else -> displayFileContentTextView.setTextColor(
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
                        displayFileContentTextView.setTextSize(
                            TypedValue.COMPLEX_UNIT_SP,
                            resources.getDimension(R.dimen.small_text_size)
                        )
                    }
                    TextSize.MIDDLE.name -> {
                        displayFileContentTextView.setTextSize(
                            TypedValue.COMPLEX_UNIT_SP,
                            resources.getDimension(R.dimen.middle_text_size)
                        )
                    }
                    else -> {
                        displayFileContentTextView.setTextSize(
                            TypedValue.COMPLEX_UNIT_SP,
                            resources.getDimension(R.dimen.large_text_size)
                        )
                    }
                }
            }
        }
    }
}