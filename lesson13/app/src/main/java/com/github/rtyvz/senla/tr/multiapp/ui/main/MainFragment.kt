package com.github.rtyvz.senla.tr.multiapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.MainFragmentBinding

class MainFragment : Fragment() {
    private var binding: MainFragmentBinding? = null

    companion object {
        const val TAG = "MainFragmentTag"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater)
        return binding?.root ?: error("can't bind main fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialog =
            InformationAboutProgramDialogFragment()
        (activity as MainActivity).changeToolbarBehavior(activity?.getString(R.string.app_name), false)
        binding?.apply {
            activity?.let { fragmentActivity ->
                aboutAuthorButton.setOnClickListener {
                    dialog.apply {
                        arguments = setDataIntoBundle(
                            fragmentActivity.getString(R.string.main_fragment_about_author_label),
                            fragmentActivity.getString(R.string.main_fragment_information_about_author)
                        )
                    }
                    dialog.show(
                        childFragmentManager,
                        InformationAboutProgramDialogFragment.TAG
                    )
                }

                aboutProgramButton.setOnClickListener {
                    dialog.apply {
                        arguments = setDataIntoBundle(
                            fragmentActivity.getString(R.string.main_fragment_about_program_label),
                            fragmentActivity.getString(R.string.main_fragment_about_program)
                        )
                    }
                    dialog.show(
                        childFragmentManager,
                        InformationAboutProgramDialogFragment.TAG
                    )
                }

                closeButton.setOnClickListener {
                    activity?.let {
                        val confirmDialog =
                            ConfirmExitDialogFragment {
                                (it as MainActivity).finish()
                            }
                        confirmDialog.apply {
                            arguments = setDataIntoBundle(
                                fragmentActivity.getString(R.string.main_fragment_are_you_sure_label)
                            )
                        }
                        confirmDialog.show(childFragmentManager, ConfirmExitDialogFragment.TAG)
                    }
                }
            }
        }
    }

    private fun setDataIntoBundle(
        title: String,
        message: String
    ): Bundle {
        return Bundle().apply {
            putString(
                InformationAboutProgramDialogFragment.EXTRA_DIALOG_TITLE,
                title
            )
            putString(
                InformationAboutProgramDialogFragment.EXTRA_DIALOG_MESSAGE,
                message
            )
        }
    }

    private fun setDataIntoBundle(title: String): Bundle {
        return Bundle().apply {
            putString(
                InformationAboutProgramDialogFragment.EXTRA_DIALOG_TITLE,
                title
            )
        }
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }
}