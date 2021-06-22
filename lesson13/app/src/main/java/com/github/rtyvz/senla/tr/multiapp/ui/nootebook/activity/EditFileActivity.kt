package com.github.rtyvz.senla.tr.multiapp.ui.nootebook.activity

import android.os.Bundle
import android.view.MenuItem
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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.editFragmentContainer, EditPaperNotebookFragment()
                .apply {
                    arguments = Bundle()
                        .apply {
                            putString(
                                EditPaperNotebookFragment.EXTRA_FILE_PATH,
                                intent?.getStringExtra(EditPaperNotebookFragment.EXTRA_FILE_PATH)
                            )
                        }
                })
            .addToBackStack(EditPaperNotebookFragment.TAG)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}