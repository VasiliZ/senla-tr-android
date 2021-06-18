package com.github.rtyvz.senla.tr.multiapp.ui.nootebook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.MultiFuncApp
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.ListNotebookFragmentBinding
import com.github.rtyvz.senla.tr.multiapp.ui.main.ChangeTitleToolBarContract
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.adapter.FilesAdapter
import java.io.File

class NotebookFragment : Fragment() {
    private var binding: ListNotebookFragmentBinding? = null
    private val filesAdapter by lazy {
        FilesAdapter { path ->
            (parentFragment as ResetDataFragmentContract).setContent(path)
        }
    }

    companion object {
        val TAG = NotebookFragment::class.simpleName.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListNotebookFragmentBinding.inflate(inflater)
        return binding?.root ?: error("can't bind parent fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as ChangeTitleToolBarContract).changeToolbarBehavior(
            activity?.getString(R.string.notebook_fragment_label),
            false
        )

        binding?.apply {
            listFile.adapter = filesAdapter
            createNewFileButton.setOnClickListener {
                (parentFragment as ResetDataFragmentContract).onCreateNewFileClicked()
            }
        }
    }

    private fun getFiles(): Array<File> {
        return MultiFuncApp.INSTANCE?.getNotebookDir().let { path ->
            path?.let {
                File(path).listFiles()
            }
        } ?: emptyArray()
    }

    private fun displayViews() {
        val arrayFiles = getFiles()

        if (arrayFiles.isEmpty()) {
            binding?.apply {
                emptyContentLabel.isVisible = true
                listFile.isVisible = false
            }
        } else {
            binding?.apply {
                filesAdapter.submitList(arrayFiles.toList())
                emptyContentLabel.isVisible = false
                listFile.isVisible = true
            }
        }
    }

    override fun onResume() {
        super.onResume()

        displayViews()
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }
}

interface ResetDataFragmentContract {
    fun setContent(content: String)
    fun onCreateNewFileClicked()
}
