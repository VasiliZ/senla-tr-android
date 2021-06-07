package com.github.rtyvz.senla.tr.multiapp.ext

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.Fragment

fun Fragment.createDialog(
    context: Context,
    message: String,
    positiveButtonText: String
): AlertDialog {
    return AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton(
            positiveButtonText
        ) { dialog, _ ->
            dialog.cancel()
        }.create()
}

fun Fragment.createDialog(
    context: Context,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    action: () -> Unit
): AlertDialog {
    return AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton(
            positiveButtonText
        ) { dialog, _ ->
            dialog.cancel()
        }.setNegativeButton(negativeButtonText) { _, _ ->
            action()
        }.create()
}