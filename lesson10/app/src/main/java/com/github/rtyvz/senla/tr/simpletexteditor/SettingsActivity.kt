package com.github.rtyvz.senla.tr.simpletexteditor

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.simpletexteditor.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var colorValue: String
    private lateinit var textSizeValue: String

    companion object {
        const val PREFS_SETTING = "SETTING"
        const val PREFS_COLOR = "COLOR"
        const val PREFS_TEXT_SIZE = "TEXT_SIZE"
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences(PREFS_SETTING, Context.MODE_PRIVATE)
        binding.apply {
            textColorRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.blackColorRadioButton -> colorValue = Color.BLACK.name
                    R.id.greenColorRadioButton -> colorValue = Color.GREEN.name
                    R.id.redColorRadioButton -> colorValue = Color.RED.name
                }
            }

            textSizeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.smallTextSizeRadioButton -> textSizeValue = TextSize.SMALL.name
                    R.id.middleTextSizeRadioButton ->
                        textSizeValue = TextSize.MIDDLE.name
                    R.id.largeTextSizeRadioButton ->
                        textSizeValue = TextSize.LARGE.name
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        saveColorText(colorValue)
        saveTextSize(textSizeValue)
    }

    private fun saveTextSize(dimension: String) {
        prefs.edit().putString(PREFS_TEXT_SIZE, dimension).apply()
    }

    private fun saveColorText(colorValue: String) {
        prefs.edit().putString(PREFS_COLOR, colorValue).apply()
    }
}