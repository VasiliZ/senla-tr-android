package com.github.rtyvz.senla.tr.notebook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
        val noteBookFragment = NoteBookFragment()
        if (isListContainerAvailable()) {
            createFragment(R.id.listFileContainer, noteBookFragment)
            createFragment(R.id.contentContainer, EditFileFragment())
        } else {
            createFragment(R.id.listFileContainer, noteBookFragment)
        }
    }

    private fun isListContainerAvailable() = binding.contentContainer != null

    override fun passData(data: String?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.contentContainer)
        if (fragment is EditFileFragment) {
            fragment.setData(data)
        } else {
            startEditActivity(data)
        }
    }

    override fun createFragmentForNewFile() {
        if (isListContainerAvailable()) {
            val fragment = supportFragmentManager.findFragmentById(R.id.contentContainer)
            if (fragment is EditFileFragment) {
                fragment.setData(null)
            }
        } else {
            startEditActivity(null)
        }
    }

    private fun startEditActivity(path: String?) {
        startActivity(Intent(this, EditFileActivity::class.java).apply {
            putExtra(EditFileActivity.EXTRA_PATH_FOR_SEND, path)
        })
    }

    private fun createFragment(fragmentId: Int, fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(fragmentId, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
