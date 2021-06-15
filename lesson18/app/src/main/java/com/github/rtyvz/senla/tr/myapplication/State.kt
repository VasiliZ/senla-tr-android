package com.github.rtyvz.senla.tr.myapplication

class State {
    private var _lastCalculatedNumber: Int = 0
    val lastPrimeNumber: Int
        get() {
            return _lastCalculatedNumber
        }

    private var _lastCount: Int = 0
    val lastCount: Int
        get() {
            return _lastCount
        }

    private val _listOfData: MutableList<String> = mutableListOf()
    val listOfData: List<String>
        get() {
            return _listOfData
        }

    fun addValueToList(value: String) {
        _listOfData.add(value)
    }

    fun setLastPrimeNumber(primeNumber: Int) {
        _lastCalculatedNumber = primeNumber
    }

    fun setLastCountValue(count: Int) {
        _lastCount = count
    }
}