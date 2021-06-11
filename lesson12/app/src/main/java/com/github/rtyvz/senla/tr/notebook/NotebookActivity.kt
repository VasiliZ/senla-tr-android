package com.github.rtyvz.senla.tr.notebook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.notebook.databinding.NotebookActivityBinding

class NotebookActivity : AppCompatActivity(), PassDataToDisplayContract {
    private lateinit var binding: NotebookActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotebookActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createFragmentFromOrientation()
    }

    private fun createFragmentFromOrientation() {
        createFragment(R.id.listFileContainer, NotebookFragment())

        if (isContentContainerAvailable()) {
            createFragment(R.id.contentContainer, EditFileFragment())
        }
    }

    private fun isContentContainerAvailable() = binding.contentContainer != null

    override fun passData(data: String?) {
        if (isContentContainerAvailable()) {
            val fragment = supportFragmentManager.findFragmentById(R.id.contentContainer)
            if (fragment is EditFileFragment) {
                fragment.setData(data)
            }
        } else {
            startEditActivity(data)
        }
    }

    private fun startEditActivity(path: String?) {
        startActivity(Intent(this, EditFileActivity::class.java).apply {
            putExtra(EditFileActivity.EXTRA_PASS_DATA_TO_ACTIVITY, path)
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
