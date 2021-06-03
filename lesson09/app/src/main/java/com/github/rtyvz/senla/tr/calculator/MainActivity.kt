package com.github.rtyvz.senla.tr.calculator

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvz.senla.tr.calculator.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private var currentValue: String = EMPTY_STRING
    private val valueAdapter by lazy {
        ValueAdapter()
    }
    private lateinit var prefs: SharedPreferences

    companion object {
        const val RESULT_OK = 1
        const val RESULT_VALUE_EXTRA = "RESULT_VALUE_EXTRA"
        private const val ITEM_CAP = 5
        private const val EMPTY_STRING = ""
        private const val SAVED_VALUE_PREFS = "SAVED_VALUE_PREFS"
        private const val SAVED_VALUE = "SAVED_VALUE"
        private const val DELIMITER = ","
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = MainActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefs = getSharedPreferences(SAVED_VALUE_PREFS, Context.MODE_PRIVATE)

        if (readFromPrefs().isNotBlank()) {
            valueAdapter.insertItems(readFromPrefs().split(DELIMITER).takeLast(5))
        }

        binding.apply {
            saveButton.setOnClickListener {
                currentValue =
                    if (valueAdapter.itemCount >= ITEM_CAP && currentValue.isNotBlank()) {
                        valueAdapter.removeItem()
                        valueAdapter.insertItem(currentValue)
                        writeToPrefs(currentValue)
                        EMPTY_STRING
                    } else {
                        valueAdapter.insertItem(currentValue)
                        writeToPrefs(currentValue)
                        EMPTY_STRING
                    }
            }

            openCalcButton.setOnClickListener {
                startActivityForResult(
                    Intent(this@MainActivity, CalcActivity::class.java),
                    RESULT_OK
                )
            }

            listSavedValues.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = valueAdapter
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let {
            currentValue = it.getStringExtra(RESULT_VALUE_EXTRA) ?: EMPTY_STRING
            binding.currentValueTextView.text = currentValue
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