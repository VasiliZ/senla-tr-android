package com.github.rtyvz.senla.tr.loginapp.profile.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.loginapp.App
import com.github.rtyvz.senla.tr.loginapp.R
import com.github.rtyvz.senla.tr.loginapp.databinding.ProfileActivityBinding
import com.github.rtyvz.senla.tr.loginapp.login.ui.LoginActivity
import com.github.rtyvz.senla.tr.loginapp.utils.clearPrefs
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ProfileActivityBinding

    companion object {
        const val EXTRA_FIRST_USER_NAME = "FIRST_USER_NAME"
        const val EXTRA_LAST_USER_NAME = "LAST_USER_NAME"
        const val EXTRA_BIRTHDAY = "BIRTHDAY"
        const val EXTRA_NOTES = "NOTES"
        const val EXTRA_EMAIL = "EMAIL"
        private const val DEFAULT_STRING_VALUE = ""
        private const val DEFAULT_LONG_VALUE = 0L
        private const val COMMA = ","
        private const val DATE_FORMAT = "dd.MM.yyyy"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

        if (intent.extras == null) {
            readUserProfileFromFile(formatter)
        } else {
            setUserDataFromIntent(formatter, intent)
        }

        binding.logOutButton.setOnClickListener {
            App.INSTANCE.getUserInformationFile().delete()
            getSharedPreferences(LoginActivity.PREFS_USER_TOKEN, Context.MODE_PRIVATE).clearPrefs()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setUserDataFromIntent(formatter: SimpleDateFormat, intent: Intent) {
        binding.apply {
            emailValueTextView.text = intent.getStringExtra(EXTRA_EMAIL) ?: DEFAULT_STRING_VALUE
            firstNameValueTextView.text =
                intent.getStringExtra(EXTRA_FIRST_USER_NAME) ?: DEFAULT_STRING_VALUE
            lastNameValueTextView.text =
                intent.getStringExtra(EXTRA_LAST_USER_NAME) ?: DEFAULT_STRING_VALUE
            birthDateValueTextView.text = formatDate(
                formatter, intent.getLongExtra(
                    EXTRA_BIRTHDAY,
                    DEFAULT_LONG_VALUE
                )
            )
            notesTextView.text = formatNotes(
                intent.getStringExtra(EXTRA_NOTES)
                    ?: DEFAULT_STRING_VALUE
            )
        }
    }

    private fun readUserProfileFromFile(formatter: SimpleDateFormat) {
        val userInformation = App.INSTANCE.getUserInformationFile().readText(
            Charset.defaultCharset()
        )
        val listUserInformation = userInformation.split(COMMA)
        binding.apply {
            emailValueTextView.text = listUserInformation[0]
            firstNameValueTextView.text = listUserInformation[1]
            lastNameValueTextView.text = listUserInformation[2]
            birthDateValueTextView.text = formatDate(
                formatter,
                listUserInformation[3].toLong()
            )
            notesTextView.text = formatNotes(listUserInformation[4])
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