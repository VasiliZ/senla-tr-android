package com.github.rtyvz.senla.tr.myapplication

data class State(
    var lastCalculatedNumber: Int = 0,
    var valueOfCount: Int = 0,
    val listOfData: MutableList<String> = mutableListOf()
)