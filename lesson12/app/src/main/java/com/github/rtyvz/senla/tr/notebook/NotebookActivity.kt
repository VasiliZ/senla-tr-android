package com.github.rtyvz.senla.tr.notebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.github.rtyvz.senla.tr.notebook.databinding.NotebookActivityBinding

class NotebookActivity : AppCompatActivity(), OpenFragmentContract {
    private lateinit var binding: NotebookActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotebookActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createFragmentFromOrientation()
    }

    private fun createFragmentFromOrientation() {
        if (isPortOrientation()) {
            createFragment(R.id.contentContainerLand, NoteBookFragment())
        } else {
            createFragment(R.id.listFileContainer, NoteBookFragment())
            createFragment(R.id.contentContainer, EditFileFragment())
        }
    }

    private fun isPortOrientation() =
        findViewById<FragmentContainerView>(R.id.listFileContainer) == null

    override fun passData(data: String?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.contentContainerLand)
        fragment?.let {
            if (isPortOrientation()) {
                createFragment(R.id.contentContainerLand, EditFileFragment().apply {
                    arguments = Bundle().apply {
                        putString(EditFileFragment.PATH_FILE_EXTRA, data)
                    }
                })
            } else {
                createFragment(R.id.contentContainer, EditFileFragment().apply {
                    arguments = Bundle().apply {
                        putString(EditFileFragment.PATH_FILE_EXTRA, data)
                    }
                })
            }
        }
    }

    override fun createFragmentForNewFile() {
        if (isPortOrientation()) {
            createFragment(R.id.contentContainerLand, EditFileFragment())
        } else {
            createFragment(R.id.contentContainer, EditFileFragment())
        }
    }

    private fun createFragment(fragmentId: Int, fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(fragmentId, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
