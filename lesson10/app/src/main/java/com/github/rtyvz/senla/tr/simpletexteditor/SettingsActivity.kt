package com.github.rtyvz.senla.tr.simpletexteditor

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.simpletexteditor.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding
    private lateinit var prefs: SharedPreferences

    companion object {
        const val SETTING_PREFS = "SETTING_PREFS"
        const val SAVE_COLOR_PREFS = "SAVE_COLOR_PREFS"
        const val SAVE_TEXT_SIZE_PREFS = "SAVE_TEXT_SIZE_PREFS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE)
        binding.apply {
            textColorRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.blackColorRadioButton -> saveColorText(
                        resources.getColor(
                            R.color.black,
                            null
                        )
                    )
                    R.id.greenColorRadioButton -> saveColorText(
                        resources.getColor(
                            R.color.green,
                            null
                        )
                    )
                    R.id.redColorRadioButton -> saveColorText(
                        resources.getColor(
                            R.color.red,
                            null
                        )
                    )
                }
            }

            textSizeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.smallTextSizeRadioButton ->
                        saveTextSize(resources.getDimension(R.dimen.small_text_size))
                    R.id.middleTextSizeRadioButton ->
                        saveTextSize(resources.getDimension(R.dimen.middle_text_size))
                    R.id.largeTextSizeRadioButton ->
                        saveTextSize(resources.getDimension(R.dimen.large_text_size))
                }
            }

            closeSettingsButton.setOnClickListener {
                finish()
            }
        }
    }

    private fun saveTextSize(dimension: Float) {
        prefs.edit().putFloat(SAVE_TEXT_SIZE_PREFS, dimension).apply()
    }

    private fun saveColorText(color: Int) {
        prefs.edit().putInt(SAVE_COLOR_PREFS, color).apply()
    }
}