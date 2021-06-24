package com.github.rtyvz.senla.tr.myapplication.ui.profile

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.myapplication.App
import com.github.rtyvz.senla.tr.myapplication.R
import com.github.rtyvz.senla.tr.myapplication.databinding.ProfileActivityBinding
import com.github.rtyvz.senla.tr.myapplication.models.State
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.ui.login.LoginActivity
import kotlinx.android.synthetic.main.profile_activity.*
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ProfileActivityBinding
    private lateinit var userProfileReceiver: BroadcastReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    companion object {
        const val EXTRA_USER_PROFILE = "USER_PROFILE"
        const val BROADCAST_USER_PROFILE = "local:BROADCAST_USER_PROFILE"
        private const val DATE_FORMAT = "dd.MM.yyyy hh:mm"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (App.INSTANCE.state != null) {
            App.INSTANCE.state?.let {
                updateUI(it.userProfile)
            }
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        initUserProfileReceiver()
        binding.pullToRefreshLayout.setOnRefreshListener {
            App.TaskProvider.getProfileTask().executeUpdateUserProfileTask(
                App.INSTANCE.state?.token ?: "",
                App.INSTANCE.state?.email ?: ""
            )
        }

        binding.logOutButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            App.INSTANCE.state = State()
            finish()
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

    private fun initUserProfileReceiver() {
        userProfileReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                updateUI(intent?.getParcelableExtra(EXTRA_USER_PROFILE))
                binding.pullToRefreshLayout.isRefreshing = false
            }
        }

        localBroadcastManager.registerReceiver(
            userProfileReceiver, IntentFilter(
                BROADCAST_USER_PROFILE
            )
        )
    }

    private fun updateUI(profile: UserProfileEntity?) {
        profile?.apply {
            emailValueTextView.text = userEmail
            firstNameValueTextView.text = firstUserName
            lastNameValueTextView.text = lastUserName
            birthDateValueTextView.text = formatDate(formatter, birthDate.toLong())
            notesTextView.text = formatNotes(userNotes)
        }
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(userProfileReceiver)

        super.onPause()
    }
}