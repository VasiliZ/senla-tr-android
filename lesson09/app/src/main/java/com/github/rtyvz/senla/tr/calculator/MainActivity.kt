package com.github.rtyvz.senla.tr.calculator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.calculator.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private var currentValue: String = EMPTY_STRING
    private val valueAdapter by lazy {
        ValueAdapter()
    }
    private lateinit var prefs: SharedPreferences

    companion object {
        const val EXTRA_RESULT_VALUE = "RESULT_VALUE"
        private const val REQUEST_CODE = 0
        private const val FIRST_LIST_ITEM = 0
        private const val ITEM_CAP = 5
        private const val EMPTY_STRING = ""
        private const val PREFS_CALC = "SAVED_VALUE_PREFS"
        private const val SAVED_VALUE = "SAVED_VALUE"
        private const val DELIMITER = ","
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = MainActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefs = getSharedPreferences(PREFS_CALC, Context.MODE_PRIVATE)

        if (readFromPrefs().isNotBlank()) {
            valueAdapter.insertItems(readFromPrefs().split(DELIMITER).takeLast(ITEM_CAP))
        }

        binding.apply {
            saveButton.setOnClickListener {
                if (valueAdapter.itemCount >= ITEM_CAP && currentValue.isNotBlank()) {
                    valueAdapter.removeItem(FIRST_LIST_ITEM)
                    valueAdapter.insertItem(currentValueTextView.text.toString())
                    writeToPrefs(currentValueTextView.text.toString())
                } else {
                    valueAdapter.insertItem(currentValueTextView.text.toString())
                    writeToPrefs(currentValueTextView.text.toString())
                }
                currentValue = EMPTY_STRING
            }

            openCalcButton.setOnClickListener {
                startActivityForResult(
                    Intent(this@MainActivity, CalcActivity::class.java),
                    REQUEST_CODE
                )
            }

            listSavedValues.apply {
                adapter = valueAdapter
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            data?.let {
                binding.currentValueTextView.text =
                    it.getStringExtra(EXTRA_RESULT_VALUE) ?: EMPTY_STRING
            }
        }
    }

    private fun writeToPrefs(valueForSave: String) {
        val previousSavedData = readFromPrefs()
        if (previousSavedData.isBlank()) {
            prefs.edit().putString(SAVED_VALUE, valueForSave).apply()
        } else {
            val stringBuilder =
                StringBuilder().append(readFromPrefs()).append(DELIMITER).append(valueForSave)
            prefs.edit().putString(SAVED_VALUE, stringBuilder.toString()).apply()
        }
    }

    private fun readFromPrefs() =
        prefs.getString(
            SAVED_VALUE,
            EMPTY_STRING
        ) ?: EMPTY_STRING
}