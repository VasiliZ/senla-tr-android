package com.github.rtyvz.senla.tr.multiapp.ui.nootebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.EditFileActivityBinding
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.fragments.EditPaperNotebookFragment

class EditFileActivity : AppCompatActivity() {

    private lateinit var binding: EditFileActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditFileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pathFile = intent?.getStringExtra(EditPaperNotebookFragment.EXTRA_FILE_PATH) ?: ""

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.contentContainer, EditPaperNotebookFragment()
                .apply {
                    arguments = Bundle()
                        .apply {
                            putString(EditPaperNotebookFragment.EXTRA_FILE_PATH, pathFile)
                        }
                })
            .addToBackStack(EditPaperNotebookFragment.TAG)
            .commit()
    }
}