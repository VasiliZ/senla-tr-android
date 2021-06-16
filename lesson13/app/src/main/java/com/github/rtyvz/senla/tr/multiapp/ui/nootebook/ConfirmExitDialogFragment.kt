package com.github.rtyvz.senla.tr.multiapp.ui.nootebook

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.github.rtyvz.senla.tr.multiapp.R

class ConfirmExitDialogFragment(private val action: () -> Unit) : DialogFragment() {
    private lateinit var dialogTitle: String

    companion object {
        const val TAG = "ConfirmExitDialogFragment"
        const val EXTRA_DIALOG_TITLE = "DIALOG_TITLE"
        private const val EMPTY_STRING = ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        arguments?.let {
            dialogTitle = it.getString(EXTRA_DIALOG_TITLE, EMPTY_STRING)
        }

        activity?.let {
            return AlertDialog.Builder(context)
                .setMessage(dialogTitle)
                .setPositiveButton(
                    it.getString(R.string.dialog_ok_button_label)
                ) { _, _ ->
                    action()
                }
                .setNegativeButton(it.getString(R.string.dialog_cancel_button_label)) { dialog, _ ->
                    dialog.dismiss()
                }.create()
        } ?: throw IllegalArgumentException("activity can't be null")
    }
}