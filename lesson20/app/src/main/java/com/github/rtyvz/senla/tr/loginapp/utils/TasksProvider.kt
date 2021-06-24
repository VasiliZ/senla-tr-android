package com.github.rtyvz.senla.tr.loginapp.utils

import com.github.rtyvz.senla.tr.loginapp.App
import com.github.rtyvz.senla.tr.loginapp.task.GetUserProfileTask
import com.github.rtyvz.senla.tr.loginapp.task.LoginTask

object TasksProvider {
    fun provideLoginTask() = LoginTask(App.httpClient, App.INSTANCE)
    fun provideProfileTask() = GetUserProfileTask(App.httpClient)
}