package com.github.rtyvz.senla.tr.multiapp.ui.nootebook.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.NotebookFragmentBinding
import com.github.rtyvz.senla.tr.multiapp.ui.main.ChangeTitleToolBarContract
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.EditFileActivity

class RootNotebookFragment : Fragment(), ResetDataFragmentContract {
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
    }

    private fun createFragmentFromOrientation() {
        createFragment(R.id.notebookContainer, NotebookFragment())

        if (isContentContainerAvailable()) {
            createFragment(R.id.contentContainer, EditPaperNotebookFragment())
        }
    }

    private fun createFragment(fragmentId: Int, fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(fragmentId, fragment)
        transaction.addToBackStack(fragment.tag)
        transaction.commit()
    }

    private fun isContentContainerAvailable() = binding?.contentContainer != null

    override fun setContent(content: String) {
        openNewDisplayFromContent(content)
    }

    override fun onCreateNewFileClicked() {
        openNewDisplayFromContent(null)
    }

    private fun startEditActivity(path: String?) {
        startActivity(Intent(activity, EditFileActivity::class.java).apply {
            putExtras(Bundle().apply {
                putString(
                    EditPaperNotebookFragment.EXTRA_FILE_PATH,
                    path
                )
            })
        })
    }

    private fun openNewDisplayFromContent(path: String?) {
        if (isContentContainerAvailable()) {
            val fragment = childFragmentManager.findFragmentById(R.id.contentContainer)
            if (fragment is EditPaperNotebookFragment) {
                fragment.setPath(path)
            }
        } else {
            startEditActivity(path)
        }
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }
}