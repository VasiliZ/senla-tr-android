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
import java.util.regex.Pattern

class MainFragment : Fragment() {
    //todo move this to result fragment
    private var binding: MainFragmentBinding? = null
    private val firstMatchGroup = 0
    private val secondMatchGroup = 1
    private val regexFindPrefixPhoneNumber = "8\\ \\(0[0-9]{2}\\)\\ ".toRegex()
    private val regexFindCodeInPhoneNumber = "8\\ \\(0(.*?)\\)".toRegex()
    //todo check this
    private val regexFindFourLetterWord = "\\b[A-Za-zА-Яа-я]{4}\\b".toRegex()
    private val regexFindEmptySpaces = "\\ +".toRegex()
    private val regexFindTags = "[^<one?.*>(.*)<\\/one>]".toRegex()
    private val regexFindLinks = "(\\swww\\.\\S*\\.\\w*)".toRegex()

    companion object {
        private const val DASH = "-"
        private const val EMPTY_SPACE = " "
        private const val EMPTY_STRING = ""
        private const val START_WWW_LINK = "http://"
        private const val LINE_BREAK = "\n"
        private const val TAB = "\t"
        private const val PREF_FIND_FOUR_LETTER_WORD = "find_four_letter_word"
        private const val PREF_REPLACE_SPACES = "replace_spaces"
        private const val PREF_FIND_PHONE_NUMBER = "find_phone_numbers"
        private const val PREF_FIND_ALL_WORLDS_IN_TAGS = "find_word_in_tag"
        private const val PREF_FIND_LINKS = "find_links"
        private const val FIND_PHONE_NUMBER_PATTERN =
            "8\\ \\(0[0-9]{2}\\)\\ [0-9]{3}\\-[0-9]{2}\\-[0-9]{2}"
        private const val NEW_PREFIX_PHONE_NUMBER = "+375"
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
                    //todo rename t and any
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

    private fun prepareResult(resultBuilder: StringBuilder, searchValues: String) {
        resultBuilder.clear()
        resultBuilder.append(searchValues)
    }

    private fun replacePhoneNumber(value: StringBuilder): String {
        val pattern = Pattern.compile(FIND_PHONE_NUMBER_PATTERN)
        val matcher = pattern.matcher(value)
        val resultBuilder = StringBuilder()
        while (matcher.find()) {
            val match =
                matcher.group(firstMatchGroup)?.replace(regexFindPrefixPhoneNumber) { prefix ->
                    prefix.value.trim().replace(regexFindCodeInPhoneNumber) { code ->
                        StringBuilder(NEW_PREFIX_PHONE_NUMBER).append(DASH)
                            .append(code.groups[secondMatchGroup]?.value?.trim()).toString()
                    } + DASH
                }
            resultBuilder.append(match).append(LINE_BREAK)
        }

        return resultBuilder.toString()
    }

    private fun setUppercaseToFourLetterWords(value: StringBuilder): String {
        return value.replace(regexFindFourLetterWord) {
            it.value.toUpperCase(Locale.getDefault())
        }
    }

    private fun replaceEmptySpaces(value: StringBuilder): String {
        return value.replace(regexFindEmptySpaces, DASH)
    }

    private fun findValueInTags(value: StringBuilder): String {
        return (regexFindTags.findAll(value)).joinToString {
            StringBuilder(it.value).append(LINE_BREAK)
        }
    }

    private fun replaceLinks(value: StringBuilder): String {
        return value.replace(regexFindLinks) {
            StringBuilder(TAB).append(START_WWW_LINK)
                .append(it.value.replace(EMPTY_SPACE, EMPTY_STRING))
        }
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }
}