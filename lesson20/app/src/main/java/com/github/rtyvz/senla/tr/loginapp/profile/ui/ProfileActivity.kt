package com.github.rtyvz.senla.tr.loginapp.profile.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.loginapp.App
import com.github.rtyvz.senla.tr.loginapp.R
import com.github.rtyvz.senla.tr.loginapp.State
import com.github.rtyvz.senla.tr.loginapp.databinding.ProfileActivityBinding
import com.github.rtyvz.senla.tr.loginapp.login.ui.LoginActivity
import com.github.rtyvz.senla.tr.loginapp.profile.entity.UserProfileResponse
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ProfileActivityBinding

    companion object {
        private const val DATE_FORMAT = "dd.MM.yyyy"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val state = App.INSTANCE.state.userProfile
        if (state != null) {
            updateUI(
                SimpleDateFormat(
                    DATE_FORMAT,
                    Locale.getDefault()
                ), state
            )
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.logOutButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            App.INSTANCE.state = State()
            finish()
        }
    }

    private fun updateUI(
        formatter: SimpleDateFormat,
        userProfileResponse: UserProfileResponse
    ) {
        binding.apply {
            emailValueTextView.text = userProfileResponse.email
            firstNameValueTextView.text = userProfileResponse.firstName
            lastNameValueTextView.text = userProfileResponse.lastName
            birthDateValueTextView.text = formatDate(formatter, userProfileResponse.birthDate)
            notesTextView.text = formatNotes(userProfileResponse.notes)
        }
    }

    private fun formatDate(formatter: SimpleDateFormat, millisForFormat: Long): String {
        return formatter.format(Date(millisForFormat))
    }

    private fun formatNotes(stringForFormat: String): String {
        return String.format(
            resources.getString(R.string.profile_activity_notes_label),
            stringForFormat
        )
    }
}
