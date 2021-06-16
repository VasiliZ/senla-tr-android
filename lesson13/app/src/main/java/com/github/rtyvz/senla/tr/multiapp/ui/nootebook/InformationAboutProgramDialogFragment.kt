package com.github.rtyvz.senla.tr.multiapp.ui.nootebook

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.github.rtyvz.senla.tr.multiapp.R

class InformationAboutProgramDialogFragment : DialogFragment() {
    private lateinit var dialogTitle: String
    private lateinit var dialogMessage: String

    companion object {
        const val TAG = "ConfirmDialogFragment"
        const val EXTRA_DIALOG_TITLE = "DIALOG_TITLE"
        const val EXTRA_DIALOG_MESSAGE = "DIALOG_MESSAGE"
        private const val EMPTY_STRING = ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            dialogMessage = it.getString(EXTRA_DIALOG_MESSAGE, EMPTY_STRING)
            dialogTitle = it.getString(EXTRA_DIALOG_TITLE, EMPTY_STRING)
        }

        activity?.let {
            return AlertDialog.Builder(it)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setPositiveButton(it.resources
                    .getString(R.string.dialog_ok_button_label)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
        } ?: throw IllegalArgumentException("activity can't be null")
    }
}