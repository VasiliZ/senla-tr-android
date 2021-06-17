package com.github.rtyvz.senla.tr.multiapp.ui.main

import android.graphics.drawable.Drawable

interface ChangeTitleToolBarContract {
    fun changeTitleToolBar(title: String?)
    fun changeToolbarNavIcon(drawable: Drawable?)
}