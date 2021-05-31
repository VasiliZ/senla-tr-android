package com.github.rtyvz.senla.tr.android.tetris

class GameArea {
    private var currentScore: Long = 0L
    private var bestScore: Long = 0L
    private var nextShape: Shape? = null
    private var speed: Byte = 0
    private var level: Byte = 0
    private val cells = arrayOf<Cell>()

    fun startGame() {}
    fun pause() {}
    fun resume() {}
    fun mute() {}
    fun unMute() {}

}