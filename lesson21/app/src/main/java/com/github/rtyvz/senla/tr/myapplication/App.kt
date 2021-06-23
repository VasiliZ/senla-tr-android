package com.github.rtyvz.senla.tr.myapplication

import android.app.Application
import com.github.rtyvz.senla.tr.myapplication.models.State

class App : Application() {
    private val state: State = State()

    companion object {
        lateinit var INSTANCE: App
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
    }

    fun getState() = state
}