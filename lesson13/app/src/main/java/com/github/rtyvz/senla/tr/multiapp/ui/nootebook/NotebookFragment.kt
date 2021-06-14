package com.github.rtyvz.senla.tr.multiapp.ui.nootebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.MultiFuncApp
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.NotebookFragmentBinding
import com.github.rtyvz.senla.tr.multiapp.ext.bool
import com.github.rtyvz.senla.tr.multiapp.ui.MainActivity
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.adapter.FilesAdapter
import java.io.File

class NotebookFragment : Fragment() {
    private var binding: NotebookFragmentBinding? = null
    private val filesAdapter by lazy {
        FilesAdapter { path ->
            if (R.bool.isLand.bool(requireContext())) {
                (parentFragment as ResetDataFragmentContract).setContent(path)
            } else {
                (activity as ResetDataFragmentContract).setContent(path)
            }
        }
    }

    companion object {
        const val TAG = "NoteBookFragment"
    }

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

        if (!R.bool.isLand.bool(requireContext())) {
            (activity as MainActivity).changeToolBar(activity?.getString(R.string.notebook_fragment_label))
        }
        binding?.apply {
            listFile.apply {
                adapter = filesAdapter
            }

            createNewFileButton.setOnClickListener {
                if (R.bool.isLand.bool(requireContext())) {
                    (parentFragment as ResetDataFragmentContract).openNewFile()
                } else {
                    (activity as ResetDataFragmentContract).openNewFile()
                }
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