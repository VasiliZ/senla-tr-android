package com.github.rtyvz.senla.tr.notebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.notebook.databinding.EditFileActivityBinding

class EditFileActivity : AppCompatActivity() {
    private lateinit var binding: EditFileActivityBinding
    private var path: String? = null

    companion object {
        const val EXTRA_PATH_FOR_SEND = "PATH_FOR_SEND"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditFileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            path = it.getStringExtra(EXTRA_PATH_FOR_SEND)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activityFragmentContainerView, EditFileFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(EditFileFragment.PATH_FILE_EXTRA, path)
                    }
                }).commit()
    }
}