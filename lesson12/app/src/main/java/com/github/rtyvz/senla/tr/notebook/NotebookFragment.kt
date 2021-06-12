package com.github.rtyvz.senla.tr.notebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.notebook.databinding.NotebookFragmentBinding
import java.io.File

class NotebookFragment : Fragment() {
    private var binding: NotebookFragmentBinding? = null
    private val filesAdapter by lazy {
        FilesAdapter { path ->
            (activity as PassDataToDisplayContract).passData(path)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotebookFragmentBinding.inflate(inflater)
        return binding?.root ?: error("can't bind ${NotebookFragment::class.java.canonicalName}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            listFile.apply {
                adapter = filesAdapter
            }
            createNewFileButton.setOnClickListener {
                (activity as PassDataToDisplayContract).createNewFile()
            }
        }
    }

    private fun getFiles(): Array<File> {
        return NotebookApp.INSTANCE?.getNotebookDir()?.let {
            File(it).listFiles()
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

interface PassDataToDisplayContract {
    fun passData(data: String?)
    fun createNewFile()
}