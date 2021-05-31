package com.github.rtyvz.senla.tr.android.tetris

abstract class Shape {
    val controller = ShapeController()

    abstract fun draw()
}