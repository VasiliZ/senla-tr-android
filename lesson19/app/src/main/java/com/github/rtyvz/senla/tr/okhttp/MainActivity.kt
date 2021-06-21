package com.github.rtyvz.senla.tr.okhttp

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.okhttp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TaskCallbacks {
    private lateinit var binding: ActivityMainBinding
    private lateinit var progress: ProgressDialog
    private var isRotate: Boolean = false

    companion object {
        private const val EXTRA_TEXT_EDIT_VALUE: String = "TEXT_EDIT_VALUE"
        private const val EXTRA_RESULT_VALUE: String = "RESULT_VALUE"
        private const val STRING_DEFAULT_VALUE = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            binding.inputValueTextEdit.setText(
                it.getString(
                    EXTRA_TEXT_EDIT_VALUE,
                    STRING_DEFAULT_VALUE
                )
            )
            binding.responseTextView.text = it.getString(EXTRA_RESULT_VALUE, STRING_DEFAULT_VALUE)
        }

        initProgressDialog()

        if (isRotate) {
            progress.show()
        }

        binding.apply {
            sendButton.setOnClickListener {
                if (!progress.isShowing) {
                    progress.show()
                }
                var fragment = supportFragmentManager.findFragmentByTag(HandleTaskFragment.TAG)
                if (fragment == null) {
                    fragment = HandleTaskFragment().apply {
                        arguments = Bundle().apply {
                            putString(
                                HandleTaskFragment.EXTRA_INPUT_VALUE,
                                binding.inputValueTextEdit.text.toString()
                            )
                        }
                    }
                    supportFragmentManager.beginTransaction().add(fragment, HandleTaskFragment.TAG)
                        .commit()
                } else {
                    (fragment as HandleTaskFragment).restartTask(binding.inputValueTextEdit.text.toString())
                }
            }
        }
    }

    private fun initProgressDialog() {
        progress = ProgressDialog(this@MainActivity)
        progress.setMessage(getString(R.string.wait))
        progress.setCanceledOnTouchOutside(false)
        progress.isIndeterminate = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_TEXT_EDIT_VALUE, binding.inputValueTextEdit.text.toString())
        outState.putString(EXTRA_RESULT_VALUE, binding.responseTextView.text.toString())
    }

    override fun taskRunningYet() {
        isRotate = true
    }

    override fun onPostExecute(it: String) {
        progress.dismiss()
        isRotate = false
        binding.responseTextView.text = it
    }

    override fun onDestroy() {
        progress.dismiss()

        super.onDestroy()
    }
}