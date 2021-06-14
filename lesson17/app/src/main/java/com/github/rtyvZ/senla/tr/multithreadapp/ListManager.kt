package com.github.rtyvZ.senla.tr.multithreadapp

class ListManager {
    private val list = mutableListOf<String>()
    private val lock = Object()

    fun setData(value: String) {
        synchronized(lock) {
            list.add(value)
            lock.notifyAll()
        }
    }

    fun getData(): List<String>? {
        synchronized(lock) {
            val copyOfList = list.toList()
            deleteData()
            return copyOfList
        }
    }

    private fun deleteData() {
        synchronized(lock) {
            list.clear()
        }
    }
}