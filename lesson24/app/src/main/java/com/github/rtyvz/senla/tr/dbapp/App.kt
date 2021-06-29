package com.github.rtyvz.senla.tr.dbapp

import android.app.Application
import com.github.rtyvz.senla.tr.dbapp.provider.DbDataProvider
import com.github.rtyvz.senla.tr.dbapp.provider.DbProvider

class App : Application() {

    companion object {
        lateinit var INSTANCE: App
        const val DB_NAME = "app.db"
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        if (!isDatabaseExists()) {
            DbDataProvider.populateDB(DbProvider.provideDb())
        }
    }

    private fun isDatabaseExists(): Boolean {
        return getDatabasePath(DB_NAME).exists()
    }
}