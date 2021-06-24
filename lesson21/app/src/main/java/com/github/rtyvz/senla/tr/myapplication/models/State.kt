package com.github.rtyvz.senla.tr.myapplication.models

data class State(
    var isTaskRunning: Boolean = false,
    var userProfile: UserProfileEntity? = null,
    var token: String = "",
    var email: String = ""
)