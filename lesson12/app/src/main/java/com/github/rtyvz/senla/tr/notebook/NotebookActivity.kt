package com.github.rtyvz.senla.tr.notebook

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.notebook.databinding.NotebookActivityBinding

class NotebookActivity : AppCompatActivity(), PassDataContract {
    private lateinit var binding: NotebookActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotebookActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            createFragment(R.id.listFileContainer, NoteBookFragment())
            createFragment(R.id.contentContainer, EditFileFragment())
        } else {
            createFragment(R.id.listFileContainer, NoteBookFragment())
        }
    }

    private fun createFragment(fragmentId: Int, fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(fragmentId, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun passData(data: String) {
        val fragment = supportFragmentManager.findFragmentById(R.id.contentContainer)
        if (fragment != null) {
            (fragment as EditFileFragment).setPath(data)
        } else {
            createFragment(R.id.listFileContainer, EditFileFragment().apply {
                arguments = Bundle().apply {
                    putString(EditFileFragment.PATH_FILE_EXTRA, data)
                }
            })
        }
    }
}
