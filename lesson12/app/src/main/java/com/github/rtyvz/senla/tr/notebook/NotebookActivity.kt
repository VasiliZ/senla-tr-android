package com.github.rtyvz.senla.tr.notebook

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

        transaction.replace(R.id.fragmentContainer, NoteBookFragment(), NoteBookFragment.TAG)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}