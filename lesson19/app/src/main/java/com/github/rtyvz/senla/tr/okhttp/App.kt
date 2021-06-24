package com.github.rtyvz.senla.tr.okhttp

import android.app.Application

class App : Application() {

    private val state = State()

    companion object {
        lateinit var INSTANCE: App
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
    }

    fun getState() = state
}