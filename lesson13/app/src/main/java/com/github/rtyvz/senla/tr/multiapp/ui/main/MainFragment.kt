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
         val TAG: String = MainFragment::class.java.simpleName
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

        (activity as ChangeTitleToolBarContract).changeToolbarBehavior(
            getString(R.string.app_name),
            false
        )

        binding?.apply {
            aboutAuthorButton.setOnClickListener {
                val dialog = InformationAboutProgramDialogFragment().apply {
                    arguments = configureBundle(
                        getString(R.string.main_fragment_about_author_label),
                        getString(R.string.main_fragment_information_about_author)
                    )
                }
                dialog.show(
                    childFragmentManager,
                    InformationAboutProgramDialogFragment.TAG
                )
            }

            aboutProgramButton.setOnClickListener {
                val dialog = InformationAboutProgramDialogFragment().apply {
                    arguments = configureBundle(
                        getString(R.string.main_fragment_about_program_label),
                        getString(R.string.main_fragment_about_program)
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
                            //TODO  create interface for this
                            (it as MainActivity).finish()
                        }
                    confirmDialog.apply {
                        arguments = setDataIntoBundle(
                            getString(R.string.main_fragment_are_you_sure_label)
                        )
                    }
                    confirmDialog.show(childFragmentManager, ConfirmExitDialogFragment.TAG)
                }
            }
        }
    }

    //todo replace this with help kotlin
    private fun configureBundle(
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