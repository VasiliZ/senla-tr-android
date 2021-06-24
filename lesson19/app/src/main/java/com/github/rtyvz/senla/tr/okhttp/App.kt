package com.github.rtyvz.senla.tr.okhttp

import android.app.Application

class App : Application() {

    var state: State? = null

    companion object {
        lateinit var INSTANCE: App
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
    }
}