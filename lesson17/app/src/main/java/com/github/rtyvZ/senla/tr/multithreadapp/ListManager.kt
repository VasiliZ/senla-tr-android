package com.github.rtyvZ.senla.tr.multithreadapp

class ListManager {
    private val list = mutableListOf<String>()

    fun setData(value: String) {
        synchronized(this) {
            list.add(value)
        }
    }

    fun getData(): List<String>? {
        synchronized(this) {
            val copyOfList = list.toList()
            deleteData()
            return copyOfList
        }
    }

    private fun deleteData() {
        synchronized(this) {
            list.clear()
        }
    }
}