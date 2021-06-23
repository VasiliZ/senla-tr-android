package com.github.rtyvz.senla.tr.myapplication.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.myapplication.databinding.ProfileActivityBinding
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.ui.login.LoginActivity
import com.github.rtyvz.senla.tr.myapplication.utils.clearProfilePrefs
import kotlinx.android.synthetic.main.profile_activity.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ProfileActivityBinding
    private lateinit var prefs: SharedPreferences

    companion object {
        const val EXTRA_USER_PROFILE = "USER_PROFILE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences(LoginActivity.PREFS_USER_TOKEN, Context.MODE_PRIVATE)
        intent?.let {
            it.getParcelableExtra<UserProfileEntity>(EXTRA_USER_PROFILE)?.apply {
                emailValueTextView.text = userEmail
                firstNameValueTextView.text = firstUserName
                lastNameValueTextView.text = lastUserName
                birthDateValueTextView.text = birthDate
                notesTextView.text = userNotes
            }
        }

        binding.logOutButton.setOnClickListener {
            prefs.clearProfilePrefs()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}