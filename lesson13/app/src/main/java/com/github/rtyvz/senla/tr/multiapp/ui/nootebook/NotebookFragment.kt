package com.github.rtyvz.senla.tr.multiapp.ui.nootebook

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drawer.ui.nootebook.EditFileFragment
import com.example.drawer.ui.nootebook.adapter.FilesAdapter
import com.github.rtyvz.senla.tr.multiapp.MainActivity
import com.github.rtyvz.senla.tr.multiapp.MultiFuncApp
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.NotebookFragmentBinding
import java.io.File

class NotebookFragment : Fragment() {
    private var binding: NotebookFragmentBinding? = null
    private val filesAdapter by lazy {
        FilesAdapter { path ->

            if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val fragment = ParentFragmentNotebook()
                fragment.arguments = Bundle().apply {
                    putString(EditFileFragment.PATH_FILE_EXTRA, path)
                }
                createFragment(R.id.fragmentContainer, fragment)
            } else {
                val fragment = EditFileFragment()
                fragment.arguments = Bundle().apply {
                    putString(EditFileFragment.PATH_FILE_EXTRA, path)
                }
                createFragment(R.id.fragmentContainer, fragment)
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
        binding = NotebookFragmentBinding.inflate(layoutInflater)
        return binding?.root ?: error("can't bind note book fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).changeToolBar(activity?.getString(R.string.notebook_fragment_label))
        binding?.apply {
            listFile.apply {
                layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                adapter = filesAdapter
            }

            createNewFileButton.setOnClickListener {

                if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    createFragment(R.id.fragmentContainer, ParentFragmentNotebook())
                } else {
                    createFragment(R.id.fragmentContainer, EditFileFragment())
                }
            }
        }
    }

    private fun getFiles(): Array<File> {
        return MultiFuncApp.INSTANCE!!.getNotebookDir().let {
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

    private fun createFragment(contentContainer: Int, fragment: Fragment) {
        val fragmentManager = childFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(contentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}