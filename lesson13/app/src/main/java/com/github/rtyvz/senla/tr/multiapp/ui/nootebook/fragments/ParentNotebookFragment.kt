package com.github.rtyvz.senla.tr.multiapp.ui.nootebook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.NotebookFragmentBinding
import com.github.rtyvz.senla.tr.multiapp.ui.main.ChangeTitleToolBarContract

class ParentNotebookFragment : Fragment(), ResetDataFragmentContract {
    private var binding: NotebookFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotebookFragmentBinding.inflate(inflater)
        return binding?.root ?: error("can't bind note book fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        createFragmentFromOrientation()

        (activity as ChangeTitleToolBarContract).changeToolbarBehavior(
            activity?.getString(R.string.notebook_fragment_label),
            false
        )
    }

    private fun createFragmentFromOrientation() {
        if (isContentContainerAvailable()) {
            createFragment(R.id.contentContainer, EditPaperNotebookFragment())
        }

        createFragment(R.id.notebookContainer, NotebookFragment())
    }

    private fun createFragment(fragmentId: Int, fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(fragmentId, fragment)
        transaction.addToBackStack(fragment.tag)
        transaction.commit()
    }

    private fun isContentContainerAvailable() = binding?.contentContainer != null

    override fun setContent(content: String) {
        val fragment = childFragmentManager.findFragmentByTag(EditPaperNotebookFragment.TAG)
        if (fragment is EditPaperNotebookFragment) {
            fragment.setPath(content)
        } else {
            createFragment(R.id.notebookContainer,
                EditPaperNotebookFragment()
                    .apply {
                        arguments = Bundle().apply {
                            putString(EditPaperNotebookFragment.EXTRA_FILE_PATH, content)
                        }
                    })
        }
    }

    override fun onCreateNewFileClicked() {
        if (isContentContainerAvailable()) {
            val fragment = childFragmentManager.findFragmentById(R.id.contentContainer)
            if (fragment is EditPaperNotebookFragment) {
                fragment.setPath(null)
            }
        } else {
            createFragment(R.id.notebookContainer, EditPaperNotebookFragment())
        }
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }
}