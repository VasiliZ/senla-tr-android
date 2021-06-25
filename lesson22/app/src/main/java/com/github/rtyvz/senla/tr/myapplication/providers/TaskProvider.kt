package com.github.rtyvz.senla.tr.myapplication.providers

import com.github.rtyvz.senla.tr.myapplication.App

object TaskProvider {
    fun getTokenTask() = GetTokenTask(App.api)
    fun getProfileTask() = UpdateProfileTask(App.api)
}