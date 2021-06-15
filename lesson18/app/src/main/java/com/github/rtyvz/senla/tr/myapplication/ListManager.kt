package com.github.rtyvz.senla.tr.myapplication

class ListManager {
    @Volatile
    private var list = mutableListOf<String>()
    private val lock = Object()

    fun setData(value: String) {
        synchronized(lock) {
            list.add(value)
        }
    }

    fun getData(): List<String>? {
        synchronized(lock) {
            val tempList = mutableListOf<String>()
            list.listIterator().forEach {
                tempList.add(it)
            }
            list.clear()
            return tempList
        }
    }
}