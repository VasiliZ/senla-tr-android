package com.github.rtyvz.senla.tr.loginapp

import com.github.rtyvz.senla.tr.loginapp.profile.entity.UserProfileResponse

data class State(
    var isTasksRunning: Boolean = false,
    var userProfile: UserProfileResponse? = null
)