package com.github.rtyvz.senla.tr.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.calculator.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var previousOperations: StringBuilder = StringBuilder()
    private var currentNumber: StringBuilder = StringBuilder()
    private var currentOperation: String = EMPTY_STRING
    private var isCalculated = false
    private val stackOperations = Stack<String>()
    private val stackForCalculation = Stack<String>()

    companion object {
        private const val EMPTY_STRING = ""
        private const val MULTIPLY_STRING = "*"
        private const val MINUS_STRING = "-"
        private const val PLUS_STRING = "+"
        private const val DIV_STRING = "/"
        private const val DIV_CHAR = '/'
        private const val MINUS_CHAR = '-'
        private const val PLUS_CHAR = '+'
        private const val MULTIPLY_CHAR = '*'
        private const val CURRENT_INPUTED_VALUE = "CURRENT_INPUTED_VALUE"
        private const val ALL_PREVIOUS_OPERATIONS = "ALL_PREVIOUS_OPERATIONS"
        private const val STACK_OPERATIONS = "STACK_OPERATIONS"
        private const val STACK_FOR_CALC = "STACK_FOR_CALC"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            currentNumber = StringBuilder(it.getString(CURRENT_INPUTED_VALUE)!!)
            previousOperations = StringBuilder(it.getString(ALL_PREVIOUS_OPERATIONS)!!)
            it.getStringArrayList(STACK_OPERATIONS)?.let { list ->
                stackOperations.addAll(list)
            }

            it.getStringArrayList(STACK_FOR_CALC)?.let { list ->
                stackForCalculation.addAll(list)
            }
            binding.apply {
                resultOfOperations.text = currentNumber
                previousOperations.text = this@MainActivity.previousOperations
            }
        }

        binding.apply {
            inputZeroButton.setOnClickListener { inputValue(getString(R.string.zero)) }
            inputEquallyButton.setOnClickListener { calculate() }
            inputPlusButton.setOnClickListener { inputOperation(getString(R.string.plus)) }
            inputOneButton.setOnClickListener { inputValue(getString(R.string.one)) }
            inputTwoButton.setOnClickListener { inputValue(getString(R.string.two)) }
            inputThreeButton.setOnClickListener { inputValue(getString(R.string.three)) }
            inputMinusButton.setOnClickListener { inputOperation(getString(R.string.minus)) }
            inputFourButton.setOnClickListener { inputValue(getString(R.string.four)) }
            inputFiveButton.setOnClickListener { inputValue(getString(R.string.five)) }
            inputSixButton.setOnClickListener { inputValue(getString(R.string.six)) }
            inputMultiplyButton.setOnClickListener { inputOperation(getString(R.string.multiply)) }
            inputSevenButton.setOnClickListener { inputValue(getString(R.string.seven)) }
            inputEightButton.setOnClickListener { inputValue(getString(R.string.eight)) }
            inputNineButton.setOnClickListener { inputValue(getString(R.string.nine)) }
            inputDivButton.setOnClickListener { inputOperation(getString(R.string.div)) }
            clearButton.setOnClickListener { clear() }
        }
    }

    private fun inputValue(value: String) {

        if (isCalculated) {
            binding.previousOperations.text = EMPTY_STRING
            previousOperations.clear()
            isCalculated = false
        }

        binding.apply {
            this@MainActivity.previousOperations.append(value)
            currentNumber.append(value)
            binding.resultOfOperations.text = currentNumber.toString()
        }

        if (currentOperation.isNotBlank()) {
            stackOperations.push(currentOperation)
            currentOperation = EMPTY_STRING
        }
    }

    private fun clear() {
        binding.apply {
            resultOfOperations.text = EMPTY_STRING
            previousOperations.text = EMPTY_STRING
            stackForCalculation.clear()
            currentNumber.clear()
            stackOperations.clear()
            this@MainActivity.previousOperations.clear()
        }
    }

    private fun calculate() {

        if (stackOperations.size < 2) {
            return
        }

        stackForCalculation.clear()
        stackOperations.reverse()
        binding.previousOperations.text = previousOperations.toString()

        if (currentNumber.isNotBlank() && !isCalculated) {
            stackOperations.push(currentNumber.toString())
        }

        while (stackOperations.isNotEmpty()) {
            val value = stackOperations.pop()

            if (isNumber(value)) {
                stackForCalculation.push(value)
            } else {
                val firstOperator = stackForCalculation.pop().toInt()
                val secondOperator = stackForCalculation.pop().toInt()

                when (value) {
                    MINUS_STRING -> {
                        stackForCalculation.push((firstOperator - secondOperator).toString())
                    }
                    PLUS_STRING -> {
                        stackForCalculation.push((firstOperator + secondOperator).toString())
                    }
                    DIV_STRING -> {
                        if (firstOperator == 0 || secondOperator == 0) {
                            previousOperations.clear()
                            stackForCalculation.clear()
                            stackOperations.clear()
                            binding.apply {
                                resultOfOperations.text = getString(R.string.error)
                                previousOperations.text = EMPTY_STRING
                            }
                        } else {
                            stackForCalculation.push((firstOperator / secondOperator).toString())
                        }
                    }
                    MULTIPLY_STRING -> {
                        stackForCalculation.push((firstOperator * secondOperator).toString())
                    }
                }
            }
        }

        if (stackForCalculation.isNotEmpty()) {
            binding.resultOfOperations.text = stackForCalculation.pop()
            stackForCalculation.clear()
            previousOperations.clear()
            currentNumber.clear()
            isCalculated = true
        }
    }

    private fun isNumber(value: String): Boolean {
        return value.toIntOrNull() != null
    }

    private fun inputOperation(operation: String) {

        if (isCalculated) {
            return
        }

        if (previousOperations.isEmpty()) {
            return
        }

        when (previousOperations.last()) {
            MINUS_CHAR -> {
                return
            }
            PLUS_CHAR -> {
                return
            }
            DIV_CHAR -> {
                return
            }
            MULTIPLY_CHAR -> {
                return
            }
        }

        stackOperations.push(currentNumber.toString())
        currentNumber.clear()
        previousOperations.append(operation)
        currentOperation = operation
        binding.previousOperations.text = previousOperations.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(CURRENT_INPUTED_VALUE, currentNumber.toString())
        outState.putString(ALL_PREVIOUS_OPERATIONS, previousOperations.toString())
        outState.putStringArrayList(STACK_OPERATIONS, ArrayList(stackOperations))
        outState.putStringArrayList(STACK_FOR_CALC, ArrayList(stackForCalculation))
    }
}