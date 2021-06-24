package com.github.rtyvz.senla.tr.myapplication.providers

import com.github.rtyvz.senla.tr.myapplication.App

object TaskProvider {
    fun getTokenTask() = GetTokenTask(App.okHttpClient, App.INSTANCE, App.gson)
    fun getProfileTask() = UpdateProfileTask(App.okHttpClient, App.gson, App.INSTANCE)
}