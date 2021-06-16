package com.github.rtyvz.senla.tr.okhttp

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.okhttp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TaskCallbacks {
    var binding: ActivityMainBinding? = null
    private var progress: ProgressDialog? = null

    companion object {
        private const val EXTRA_TEXT_EDIT_VALUE: String = "TEXT_EDIT_VALUE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        savedInstanceState?.let {
            binding?.inputValueTextEdit?.setText(it.getString(EXTRA_TEXT_EDIT_VALUE, ""))
        }

        initProgressDialog()

        binding?.apply {
            sendButton.setOnClickListener {
                var fragment = supportFragmentManager.findFragmentByTag(HandleTaskFragment.TAG)
                if (fragment == null) {
                    fragment = HandleTaskFragment()
                    supportFragmentManager.beginTransaction().add(fragment, HandleTaskFragment.TAG)
                        .commit()
                }
            }
        }
    }

    private fun initProgressDialog() {
        progress = ProgressDialog(this@MainActivity)
        progress?.setMessage(getString(R.string.wait))
        progress?.setCanceledOnTouchOutside(false)
        progress?.isIndeterminate = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_TEXT_EDIT_VALUE, binding?.inputValueTextEdit?.text.toString())
    }

    override fun taskRunningYet() {
        progress?.show()
    }

    override fun onPostExecute(it: String) {
        progress?.dismiss()
        binding?.responseTextView?.text = it
    }

    override fun onDestroy() {
        progress?.dismiss()

        super.onDestroy()
    }
}