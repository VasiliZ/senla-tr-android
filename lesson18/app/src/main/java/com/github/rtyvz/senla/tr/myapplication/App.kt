package com.github.rtyvz.senla.tr.myapplication

import android.app.Application

class App : Application() {
    private var state = State()


    companion object {
        lateinit var INSTANCE: App
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
    }

    fun getGetState() = state
}