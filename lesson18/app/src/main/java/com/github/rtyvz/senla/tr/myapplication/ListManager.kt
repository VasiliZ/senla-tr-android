package com.github.rtyvz.senla.tr.myapplication

class ListManager {
    @Volatile
    private var list = mutableListOf<String>()

    fun setData(value: String) {
        synchronized(this) {
            list.add(value)
        }
    }

    fun getData(): List<String>? {
        synchronized(this) {
            val tempList = mutableListOf<String>()
            list.listIterator().forEach {
                tempList.add(it)
            }
            list.clear()

            return tempList
        }
    }
}