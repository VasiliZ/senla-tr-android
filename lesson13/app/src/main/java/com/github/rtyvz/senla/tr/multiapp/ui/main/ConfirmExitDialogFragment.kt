package com.github.rtyvz.senla.tr.multiapp.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.github.rtyvz.senla.tr.multiapp.R

class ConfirmExitDialogFragment(private val action: () -> Unit) : DialogFragment() {

    companion object {
        val TAG: String = ConfirmExitDialogFragment::class.java.simpleName
        private const val EXTRA_DIALOG_TITLE = "DIALOG_TITLE"
        private const val EMPTY_STRING = ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(context)
            .setMessage(
                arguments?.getString(
                    EXTRA_DIALOG_TITLE,
                    EMPTY_STRING
                )
            )
            .setPositiveButton(
                context?.getString(R.string.dialog_ok_button_label)
            ) { _, _ ->
                action()
            }
            .setNegativeButton(context?.getString(R.string.dialog_cancel_button_label)) { dialog, _ ->
                dialog.dismiss()
            }.create()
    }
}