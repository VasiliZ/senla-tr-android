package com.github.rtyvz.senla.tr.regexapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.github.rtyvz.senla.tr.regexapp.databinding.MainFragmentBinding
import java.util.*

class MainFragment : Fragment() {
    private var binding: MainFragmentBinding? = null

    companion object {
        private const val DASH = "-"
        private const val EMPTY_SPACE = " "
        private const val EMPTY_STRING = ""
        private const val START_WWW_LINK = "http://"
        private const val TAB = "\\t"
        private const val PREF_FIND_FOUR_LETTER_WORD = "find_four_letter_word"
        private const val PREF_REPLACE_SPACES = "replace_spaces"
        private const val PREF_FIND_PHONE_NUMBER = "find_phone_numbers"
        private const val PREF_FIND_ALL_WORLDS_IN_TAGS = "find_word_in_tag"
        private const val PREF_FIND_LINKS = "find_links"
        private const val REGEX_FIND_PHONE_NUMBER =
            "8\\ \\(0[0-9]{2}\\)\\ [0-9]{3}\\-[0-9]{2}\\-[0-9]{2}"
        private const val REGEX_FIND_PREFIX_PHONE_NUMBER = "8\\ \\(0[0-9]{2}\\)"
        private const val REGEX_FIND_CODE_PHONE_NUMBER = "8\\ \\(0(.*?)\\)"
        private const val REGEX_NEW_PREFIX_PHONE_NUMBER = "+375"
        private const val REGEX_FIND_FOUR_LETTER_WORD = "\\b\\w[A-Za-zА-Яа-я]{3}\\b"
        private const val REGEX_FIND_EMPTY_SPACES = "\\ +"
        private const val REGEX_FIND_TAGS = "[^<one?.*>(.*)<\\/one>]"
        private const val REGEX_FIND_LINKS = "(\\swww\\.\\S*\\.\\w*)"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater)
        return binding?.root ?: error("can't bind main fragment")
    }

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
                                PREF_FIND_FOUR_LETTER_WORD -> {
                                    prepareResult(
                                        resultBuilding,
                                        setUppercaseToFourLetterWords(resultBuilding)
                                    )
                                }
                                PREF_REPLACE_SPACES -> {
                                    prepareResult(
                                        resultBuilding,
                                        replaceEmptySpaces(resultBuilding)
                                    )
                                }
                                PREF_FIND_PHONE_NUMBER -> {
                                    prepareResult(
                                        resultBuilding,
                                        replacePhoneNumber(resultBuilding)
                                    )
                                }
                                PREF_FIND_ALL_WORLDS_IN_TAGS -> {
                                    prepareResult(
                                        resultBuilding,
                                        findValueInTags(resultBuilding)
                                    )
                                }

                                PREF_FIND_LINKS -> {
                                    prepareResult(
                                        resultBuilding,
                                        replaceLinks(resultBuilding)
                                    )
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

    private fun prepareResult(resultBuilder: StringBuilder, valueForPrepare: String) {
        resultBuilder.clear()
        resultBuilder.append(valueForPrepare)
    }

    private fun replacePhoneNumber(value: StringBuilder): String {
        return value.replace(REGEX_FIND_PHONE_NUMBER.toRegex()) { number ->
            number.value.replace(REGEX_FIND_PREFIX_PHONE_NUMBER.toRegex()) { prefix ->
                prefix.value.replace(REGEX_FIND_CODE_PHONE_NUMBER.toRegex()) { code ->
                    REGEX_NEW_PREFIX_PHONE_NUMBER + DASH + code.groups[1]?.value
                } + DASH
            }
        }
    }

    private fun setUppercaseToFourLetterWords(value: StringBuilder): String {
        return value.replace(REGEX_FIND_FOUR_LETTER_WORD.toRegex()) {
            it.value.toUpperCase(Locale.ROOT)
        }
    }

    private fun replaceEmptySpaces(value: StringBuilder): String {
        return value.replace(REGEX_FIND_EMPTY_SPACES.toRegex(), DASH)
    }

    private fun findValueInTags(value: StringBuilder): String {
        return (REGEX_FIND_TAGS.toRegex().findAll(value)).joinToString {
            it.value
        }
    }

    private fun replaceLinks(value: StringBuilder): String {
        return value.replace(REGEX_FIND_LINKS.toRegex()) {
            TAB + START_WWW_LINK + it.value.replace(EMPTY_SPACE, EMPTY_STRING)
        }
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }
}