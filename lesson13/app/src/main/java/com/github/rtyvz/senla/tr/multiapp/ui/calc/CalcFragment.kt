package com.github.rtyvz.senla.tr.multiapp.ui.calc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.CalcFragmentBinding
import com.github.rtyvz.senla.tr.multiapp.ui.main.ChangeTitleToolBarContract
import java.util.*

class CalcFragment : Fragment() {
    private var binding: CalcFragmentBinding? = null
    private var previousOperations: StringBuilder = StringBuilder()
    private var currentNumber: StringBuilder = StringBuilder()
    private var currentOperation: String = EMPTY_STRING
    private var isCalculated = false
    private val stackOperations = Stack<String>()
    private val stackForCalculation = Stack<String>()

    companion object {
        const val TAG = "CalcFragmentTag"
        private const val EMPTY_STRING = ""
        private const val MULTIPLY_STRING = "*"
        private const val MINUS_STRING = "-"
        private const val PLUS_STRING = "+"
        private const val DIV_STRING = "/"
        private const val DIV_CHAR = '/'
        private const val MINUS_CHAR = '-'
        private const val PLUS_CHAR = '+'
        private const val MULTIPLY_CHAR = '*'
        private const val SAVE_CURRENT_INPUTED_VALUE = "CURRENT_INPUTED_VALUE"
        private const val SAVE_ALL_PREVIOUS_OPERATIONS = "ALL_PREVIOUS_OPERATIONS"
        private const val SAVE_STACK_OPERATIONS = "STACK_OPERATIONS"
        private const val SAVE_STACK_FOR_CALC = "STACK_FOR_CALC"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CalcFragmentBinding.inflate(inflater)
        return binding?.root ?: error("can't bind calc fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as ChangeTitleToolBarContract).changeToolbarBehavior(activity?.getString(R.string.calc_fragment_title), false)
        savedInstanceState?.let {
            currentNumber = StringBuilder(it.getString(SAVE_CURRENT_INPUTED_VALUE) ?: EMPTY_STRING)
            previousOperations =
                StringBuilder(it.getString(SAVE_ALL_PREVIOUS_OPERATIONS) ?: EMPTY_STRING)
            it.getStringArrayList(SAVE_STACK_OPERATIONS)?.let { list ->
                stackOperations.addAll(list)
            }

            it.getStringArrayList(SAVE_STACK_FOR_CALC)?.let { list ->
                stackForCalculation.addAll(list)
            }
            binding?.apply {
                resultOfOperationsTextView.text = currentNumber
                previousOperationsTextView.text = previousOperations
            }
        }

        binding?.apply {
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
        //если нажимали равно и вычислили значение то очищаем предыдущую операцию
        if (isCalculated) {
            binding?.previousOperationsTextView?.text = EMPTY_STRING
            previousOperations.clear()
            isCalculated = false
        }

        binding?.apply {
            previousOperations.append(value)
            currentNumber.append(value)
            previousOperationsTextView.text = currentNumber.toString()
        }

        //если была операция то пушим ее после введенного значния
        if (currentOperation.isNotBlank()) {
            stackOperations.push(currentOperation)
            currentOperation = EMPTY_STRING
        }
    }

    //нажали на "C" все почистили
    private fun clear() {
        binding?.apply {
            resultOfOperationsTextView.text = EMPTY_STRING
            previousOperationsTextView.text = EMPTY_STRING
            stackForCalculation.clear()
            currentNumber.clear()
            stackOperations.clear()
            previousOperations.clear()
        }
    }

    private fun calculate() {
        //если в стеке недостаточно значений для выполнения операции - скипаем
        if (stackOperations.size < 2) {
            return
        }
        //удаляем предыдущие вычисления если друг остались
        stackForCalculation.clear()
        //переворачиваем сте что бы было удобней брать значение по порядку
        stackOperations.reverse()
        binding?.resultOfOperationsTextView?.text = previousOperations.toString()
        //проверяем есть ли значения и состояние вычисления
        if (currentNumber.isNotBlank() && !isCalculated) {
            stackOperations.push(currentNumber.toString())
        }

        while (stackOperations.isNotEmpty()) {
            val value = stackOperations.pop()
            //проверяем значение, чифра это или нет, если цифра то пушим в отдельный стек для вычислений
            //если нет то берем два числа из стека и производим над ними манипуляции
            if (isNumber(value)) {
                stackForCalculation.push(value)
            } else {
                val firstOperator = stackForCalculation.pop().toInt()
                val secondOperator = stackForCalculation.pop().toInt()

                when (value) {
                    MINUS_STRING -> stackForCalculation.push((firstOperator - secondOperator).toString())
                    PLUS_STRING ->
                        stackForCalculation.push((firstOperator + secondOperator).toString())
                    DIV_STRING -> {
                        //проверяем деление на 0
                        if (firstOperator == 0 || secondOperator == 0) {
                            previousOperations.clear()
                            stackForCalculation.clear()
                            stackOperations.clear()
                            binding?.apply {
                                resultOfOperationsTextView.text = getString(R.string.error)
                                previousOperationsTextView.text = EMPTY_STRING
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
        //если в стеке с вычислениями есть значение после всех манипуляций
        //выводим его на экран и подчищаем за собой переменные
        if (stackForCalculation.isNotEmpty()) {
            binding?.resultOfOperationsTextView?.text = stackForCalculation.pop()
            stackForCalculation.clear()
            previousOperations.clear()
            currentNumber.clear()
            isCalculated = true
        }
    }

    private fun isNumber(value: String): Boolean {
        return value.toIntOrNull() != null
    }

    //проверям после нажатия кнопки что бы не пушить операции подряд одна за одной
    private fun inputOperation(operation: String) {

        if (isCalculated || previousOperations.isEmpty()) {
            return
        }

        when (previousOperations.last()) {
            MINUS_CHAR, PLUS_CHAR, DIV_CHAR, MULTIPLY_CHAR -> return
        }

        stackOperations.push(currentNumber.toString())
        currentNumber.clear()
        previousOperations.append(operation)
        currentOperation = operation
        binding?.resultOfOperationsTextView?.text = previousOperations.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SAVE_CURRENT_INPUTED_VALUE, currentNumber.toString())
        outState.putString(SAVE_ALL_PREVIOUS_OPERATIONS, previousOperations.toString())
        outState.putStringArrayList(SAVE_STACK_OPERATIONS, ArrayList(stackOperations))
        outState.putStringArrayList(SAVE_STACK_FOR_CALC, ArrayList(stackForCalculation))
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()

    }
}