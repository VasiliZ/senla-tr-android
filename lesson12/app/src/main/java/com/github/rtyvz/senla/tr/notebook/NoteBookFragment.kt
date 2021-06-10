package com.github.rtyvz.senla.tr.notebook

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drawer.ui.nootebook.adapter.FilesAdapter
import com.github.rtyvz.senla.tr.notebook.databinding.NotebookFragmentBinding
import java.io.File

class NoteBookFragment : Fragment() {
    private var binding: NotebookFragmentBinding? = null
    private val filesAdapter by lazy {
        FilesAdapter { path ->
            (activity as NotebookActivity).passData(path)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NotebookFragmentBinding.inflate(inflater)
        return binding?.root ?: error("can't bind ${NoteBookFragment::class.java.canonicalName}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayViews()
        binding?.apply {
            listFile.apply {
                layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                adapter = filesAdapter
            }

            createNewFileButton.setOnClickListener {

                if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    createFragment(R.id.contentContainer, EditFileFragment())
                } else {
                    createFragment(R.id.listFileContainer, EditFileFragment())
                }
            }
        }
    }

    private fun getFiles(): Array<File> {
        return NotebookApp.INSTANCE?.getNotebookDir().let {
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

    private fun createFragment(contentContainer: Int, fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(contentContainer, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
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