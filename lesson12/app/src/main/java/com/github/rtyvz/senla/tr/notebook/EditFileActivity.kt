package com.github.rtyvz.senla.tr.notebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.notebook.databinding.EditFileActivityBinding

class EditFileActivity : AppCompatActivity() {
    private lateinit var binding: EditFileActivityBinding
    private var path: String? = null

    companion object {
        const val EXTRA_FILE_PATH = "FILE_PATH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditFileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            path = it.getStringExtra(EXTRA_FILE_PATH)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, EditFileFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(EditFileFragment.EXTRA_PASS_DATA_TO_FRAGMENT, path)
                    }
                }).commit()
    }
}