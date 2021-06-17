package com.github.rtyvz.senla.tr.loginapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.loginapp.databinding.ProfileActivityBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ProfileActivityBinding
    private lateinit var firstName: String
    private lateinit var lastName: String
    private var birthday: Long = 0L
    private lateinit var notes: String

    companion object {
        const val EXTRA_FIRST_USER_NAME = "FIRST_USER_NAME"
        const val EXTRA_LAST_USER_NAME = "LAST_USER_NAME"
        const val EXTRA_BIRTHDAY = "BIRTHDAY"
        const val EXTRA_NOTES = "NOTES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            firstName = it.getStringExtra(EXTRA_FIRST_USER_NAME) ?: ""
            lastName = it.getStringExtra(EXTRA_LAST_USER_NAME) ?: ""
            birthday = it.getLongExtra(EXTRA_BIRTHDAY, 0L)
            notes = it.getStringExtra(EXTRA_NOTES) ?: ""
        }
    }
}