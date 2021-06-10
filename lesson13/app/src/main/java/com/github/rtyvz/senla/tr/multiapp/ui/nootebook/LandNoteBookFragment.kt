package com.github.rtyvz.senla.tr.multiapp.ui.nootebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drawer.ui.nootebook.EditFileFragment
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.NotebookLandFragmentBinding
import com.github.rtyvz.senla.tr.multiapp.ext.bool

class LandNoteBookFragment : Fragment(), ResetDataFragmentContract {
    private var binding: NotebookLandFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotebookLandFragmentBinding.inflate(inflater)
        return binding?.root ?: error("can't bind Notebook land fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragment(R.id.listFileContainer, NotebookFragment())
        setFragment(R.id.contentContainer, EditFileFragment())
    }

    private fun setFragment(fragmentId: Int, fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(fragmentId, fragment)
        transaction.commit()
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }

    override fun setContent(content: String) {
        val fragment = childFragmentManager.findFragmentById(R.id.contentContainer)
        if (fragment is EditFileFragment) {
            fragment.setContent(content)
        } else {
            setFragment(R.id.contentContainer, EditFileFragment().apply {
                arguments = Bundle().apply {
                    putString(EditFileFragment.PATH_FILE_EXTRA, content)
                }
            })
        }
    }

    override fun openNewFile() {
        if (R.bool.isLand.bool(requireActivity())) {
            val fragment = childFragmentManager.findFragmentById(R.id.contentContainer)
            if (fragment is EditFileFragment) {
                fragment.setContent(null)
            }
        } else {
            setFragment(R.id.contentContainer, EditFileFragment())
        }
    }
}

interface ResetDataFragmentContract {
    fun setContent(content: String)
    fun openNewFile()
}