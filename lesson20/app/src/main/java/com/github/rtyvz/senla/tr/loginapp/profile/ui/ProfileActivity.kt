package com.github.rtyvz.senla.tr.loginapp.profile.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.loginapp.App
import com.github.rtyvz.senla.tr.loginapp.R
import com.github.rtyvz.senla.tr.loginapp.State
import com.github.rtyvz.senla.tr.loginapp.databinding.ProfileActivityBinding
import com.github.rtyvz.senla.tr.loginapp.login.ui.LoginActivity
import com.github.rtyvz.senla.tr.loginapp.profile.entity.UserProfileResponse
import com.github.rtyvz.senla.tr.loginapp.utils.clearPrefs
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ProfileActivityBinding

    companion object {
        private const val COMMA = ","
        private const val DATE_FORMAT = "dd.MM.yyyy"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

        if (App.INSTANCE.state.userProfile != null) {
            App.INSTANCE.state.userProfile?.let {
                setUserDataFromIntent(formatter, it)
            }
        } else {
            readUserProfileFromFile(formatter)
        }

        binding.logOutButton.setOnClickListener {
            App.INSTANCE.getUserInformationFile().delete()
            getSharedPreferences(LoginActivity.PREFS_USER_TOKEN, Context.MODE_PRIVATE).clearPrefs()
            startActivity(Intent(this, LoginActivity::class.java))
            App.INSTANCE.state = State()
            finish()
        }
    }

    private fun setUserDataFromIntent(
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
