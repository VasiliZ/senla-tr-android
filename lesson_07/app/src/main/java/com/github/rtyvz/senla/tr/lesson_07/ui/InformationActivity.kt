package com.github.rtyvz.senla.tr.lesson_07.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson_07.databinding.InformationActivityBinding
import com.github.rtyvz.senla.tr.lesson_07.entity.UserInformation

class InformationActivity : AppCompatActivity() {
    private lateinit var binding: InformationActivityBinding

    companion object {
        const val EXTRA_USER_INFORMATION = "user.information"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = InformationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent?.getParcelableExtra<UserInformation>(EXTRA_USER_INFORMATION)
        user?.let {
            binding.also {
                it.loginValue.text = user.login
                it.passwordValue.text = user.password
                it.nameValue.text = user.name
                it.secondNameValue.text = user.secondName
                it.sexValue.text = user.sex
                it.additionalInformationValue.text = user.additionalInformation
            }
        }

        binding.backButton.setOnClickListener {
            this.finish()
        }
    }
}
