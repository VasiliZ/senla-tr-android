package com.github.rtyvz.senla.tr.multiapp.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.github.rtyvz.senla.tr.multiapp.R

class InformationAboutProgramDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "ConfirmDialogFragment"
        const val EXTRA_DIALOG_TITLE = "DIALOG_TITLE"
        const val EXTRA_DIALOG_MESSAGE = "DIALOG_MESSAGE"
        private const val EMPTY_STRING = ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(context)
            .setTitle(
                arguments?.getString(
                    EXTRA_DIALOG_TITLE,
                    EMPTY_STRING
                )
            )
            .setMessage(
                arguments?.getString(
                    EXTRA_DIALOG_TITLE,
                    EMPTY_STRING
                )
            )
            .setPositiveButton(
                context?.resources
                    ?.getString(R.string.dialog_ok_button_label)
            ) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}