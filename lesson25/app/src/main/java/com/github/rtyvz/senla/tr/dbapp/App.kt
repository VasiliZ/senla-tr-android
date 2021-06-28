package com.github.rtyvz.senla.tr.dbapp

import android.app.Application
import android.content.Context
import com.github.rtyvz.senla.tr.dbapp.db.AppDb
import com.github.rtyvz.senla.tr.dbapp.models.CommentEntity
import com.github.rtyvz.senla.tr.dbapp.models.PostEntity
import com.github.rtyvz.senla.tr.dbapp.models.UserEntity
import com.github.rtyvz.senla.tr.dbapp.provider.DbProvider

class App : Application() {

    companion object {
        lateinit var INSTANCE: App
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
    }
}