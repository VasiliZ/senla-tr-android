package com.github.rtyvz.senla.tr.notebook

import android.text.Editable

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)