package com.github.rtyvz.senla.tr.lesson_07.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson_07.databinding.InformationActivityBinding
import com.github.rtyvz.senla.tr.lesson_07.entity.UserInformation

class InformationActivity : AppCompatActivity() {
    private lateinit var binding: InformationActivityBinding

    companion object {
        const val EXTRA_USER_INFORMATION = "USER_INFORMATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = InformationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent?.getParcelableExtra<UserInformation>(EXTRA_USER_INFORMATION)
        user?.let {
            binding.apply {
                loginValue.text = user.login
                passwordValue.text = user.password
                nameValue.text = user.name
                secondNameValue.text = user.secondName
                sexValue.text = user.sex
                additionalInformationValue.text = user.additionalInformation
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
