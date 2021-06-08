package com.github.rtyvz.senla.tr.multiapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.MainActivity
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.MainFragmentBinding
import com.github.rtyvz.senla.tr.multiapp.ext.createDialog

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

        (activity as MainActivity).changeToolBar(activity?.getString(R.string.app_name))
        binding?.apply {
            aboutAuthorButton.setOnClickListener {
                activity?.let {
                    this@MainFragment.createDialog(
                        it,
                        getString(R.string.main_fragment_information_about_author),
                        getString(R.string.dialog_ok_button_label)
                    ).show()
                }
            }

            aboutProgramButton.setOnClickListener {
                activity?.let {
                    this@MainFragment.createDialog(
                        it,
                        getString(R.string.main_fragment_about_program),
                        getString(R.string.dialog_ok_button_label)
                    ).show()
                }
            }

            closeButton.setOnClickListener {
                activity?.let {
                    this@MainFragment.createDialog(
                        it,
                        getString(R.string.main_fragment_are_you_sure_label),
                        getString(R.string.dialog_cancel_button_label),
                        getString(R.string.dialog_ok_button_label)
                    ) {
                        it.finish()
                    }.show()
                }
            }
        }
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }
}