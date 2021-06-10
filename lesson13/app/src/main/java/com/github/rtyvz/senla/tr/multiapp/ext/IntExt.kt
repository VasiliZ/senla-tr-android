package com.github.rtyvz.senla.tr.multiapp.ext

import android.content.Context

fun Int.bool(context: Context): Boolean = context.resources.getBoolean(this)