package com.github.rtyvz.senla.tr.notebook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.rtyvz.senla.tr.notebook.adapter.FilesAdapter
import com.github.rtyvz.senla.tr.notebook.databinding.NotebookActivityBinding
import java.io.File

class NotebookActivity : AppCompatActivity() {
    private lateinit var binding: NotebookActivityBinding
    private val filesAdapter by lazy {
        FilesAdapter { path ->
            startActivity(Intent(this, EditFileActivity::class.java).also {
                it.putExtras(Bundle().apply {
                    putString(EditFileActivity.PATH_FILE_EXTRA, path)
                })
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotebookActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            listFile.apply {
                layoutManager =
                    LinearLayoutManager(this@NotebookActivity, LinearLayoutManager.VERTICAL, false)
                adapter = filesAdapter
            }

            createNewFileButton.setOnClickListener {
                startActivity(Intent(this@NotebookActivity, EditFileActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()

        configureViews()
    }

    private fun getFiles(): Array<File> {
        return NotebookApp.INSTANCE?.getNotebookDirPath().let {
            File(it).listFiles()
        } ?: emptyArray()
    }

    private fun configureViews() {
        val arrayFiles = getFiles()
        if (arrayFiles.isEmpty()) {
            binding.apply {
                emptyContentLabel.isVisible = true
                listFile.isVisible = false
            }
        } else {
            binding.apply {
                filesAdapter.submitList(arrayFiles.toList())
                emptyContentLabel.isVisible = false
                listFile.isVisible = true
            }
        }
    }
}