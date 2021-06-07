package com.github.rtyvz.senla.tr.simpletexteditor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.github.rtyvz.senla.tr.simpletexteditor.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val FILE_NAME = "data.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            createFileButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, EditFileActivity::class.java))
            }
            editFileButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, EditFileActivity::class.java))
            }
            displayFileButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        DisplayFileContentActivity::class.java
                    )
                )
            }
            settingsButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
        }
    }

    private fun configureViews() {
        val filePath = filesDir.absolutePath + File.separator + FILE_NAME

        if (File(filePath).exists()) {
            binding.apply {
                createFileButton.isVisible = false
                editFileButton.isVisible = true
                displayFileButton.isVisible = true
            }
        } else {
            binding.apply {
                createFileButton.isVisible = true
                editFileButton.isVisible = false
                displayFileButton.isVisible = false
            }
        }
    }

    override fun onResume() {
        super.onResume()

        configureViews()
    }
}