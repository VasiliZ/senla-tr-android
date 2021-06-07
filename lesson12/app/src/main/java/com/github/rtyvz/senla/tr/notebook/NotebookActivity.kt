package com.github.rtyvz.senla.tr.notebook

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.notebook.databinding.NotebookActivityBinding

class NotebookActivity : AppCompatActivity() {
    private lateinit var binding: NotebookActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotebookActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        if (resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            transaction.replace(R.id.contentContainer, EditFileFragment())
            transaction.addToBackStack(null)
            transaction.replace(R.id.listFileContainer, NoteBookFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        } else {
            transaction.replace(R.id.listFileContainer, NoteBookFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
