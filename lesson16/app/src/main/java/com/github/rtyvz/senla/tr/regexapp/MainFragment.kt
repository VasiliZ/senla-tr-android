package com.github.rtyvz.senla.tr.regexapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.github.rtyvz.senla.tr.regexapp.databinding.MainFragmentBinding

class MainFragment : Fragment() {
    private var binding: MainFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater)
        return binding?.root ?: error("can't bind main fragment")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var resultBuilding: StringBuilder
        binding?.apply {
            applyButton.setOnClickListener {
                resultBuilding = StringBuilder(mainEditText.text.toString())
                activity?.let {
                    PreferenceManager.getDefaultSharedPreferences(it).all.forEach { (t, any) ->
                        if ((any as Boolean)) {
                            when (t) {
                                "find_four_letter_word" -> {
                                    val tempStringValue =
                                        findAndReplaceForLetterWord(resultBuilding)
                                    resultBuilding.clear()
                                    resultBuilding.append(tempStringValue)
                                }
                                "replace_minus" -> {
                                    val tempStringValue = findAndReplaceMinus(resultBuilding)
                                    resultBuilding.clear()
                                    resultBuilding.append(tempStringValue)
                                }
                                "find_phone_numbers" -> {
                                    val tempStringValue = findAndReplacePhoneNumber(resultBuilding)
                                    resultBuilding.clear()
                                    resultBuilding.append(tempStringValue)
                                }
                                "find_word_in_tag" -> {
                                    val tempStringValue =
                                        findAndReplaceTags(resultBuilding.toString())
                                    resultBuilding.clear()
                                    resultBuilding.append(tempStringValue)
                                }

                                "find_links" -> {
                                    val tempStringValue =
                                        findAndReplaceLinks(resultBuilding.toString())
                                    resultBuilding.clear()
                                    resultBuilding.append(tempStringValue)
                                }
                            }
                        }
                    }
                }
                startActivity(Intent(activity, ResultActivity::class.java).apply {
                    putExtra(ResultActivity.EXTRA_PARSE_RESULT, resultBuilding.toString())
                })
            }
        }
    }

    private fun findAndReplacePhoneNumber(value: StringBuilder): String {
        return value.replace("8\\ \\(0[0-9]{2}\\)\\ [0-9]{3}\\-[0-9]{2}\\-[0-9]{2}".toRegex()) {
            it.value.replace("8\\ \\(0[0-9]{2}\\)".toRegex()) {
                it.value.replace("8\\ \\(0(.*?)\\)".toRegex()) {
                    "+375-${it.groups[1]?.value}-"
                }
            }
        }
    }

    private fun findAndReplaceForLetterWord(value: StringBuilder): String {
        return value.replace("[A-Za-zа-яА-я]{4}".toRegex()) {
            it.value.toUpperCase()
        }
    }

    private fun findAndReplaceMinus(value: StringBuilder): String {
        return value.replace("\\ +".toRegex(), "-")
    }

    private fun findAndReplaceTags(value: String): String {
        return ("[^<one?.*>(.*)<\\/one>]".toRegex().findAll(value)).joinToString {
            it.value
        }
    }

    private fun findAndReplaceLinks(value: String): String {
        return value.replace("(\\swww\\.\\S*\\.\\w*)".toRegex()) {
            "\\thttp://${it.value.replace(" ", "")}"
        }
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }
}