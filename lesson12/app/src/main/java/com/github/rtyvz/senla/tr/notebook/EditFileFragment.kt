package com.github.rtyvz.senla.tr.notebook

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.notebook.databinding.EditFileFragmentBinding
import java.io.File

class EditFileFragment : Fragment(), SetDataContract {
    private var binding: EditFileFragmentBinding? = null
    private var savedFilePath: String? = null
    private var newFileNameForRepeatedFileName: String = EMPTY_STRING
    private var isNewFile = false
    private var firstLineFileIsEmpty = false
    private var countFileRepeat = 0

    companion object {
        private const val CHAR_SET = "UTF-8"
        private const val EMPTY_STRING = ""
        private const val MAX_FILE_NAME_SIZE = 30
        private const val START_STRING_INDEX = 0
        private const val FILE_EXT = ".txt"
        private const val LINE_BREAK = "\n"
        private const val FILE_NAME_REGEX = "[^A-Za-zа-яА-Я0-9() ]"
        private const val DEFAULT_FILE_NAME = "Документ"
        private const val OPEN_BRACKET = "("
        private const val CLOSE_BRACKET = ")"
        private const val EMPTY_SPACE = " "
        private const val COUNT_UP = 1
        const val PATH_FILE_EXTRA = "PATH_FILE_EXTRA"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditFileFragmentBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            savedFilePath = it.getString(PATH_FILE_EXTRA) ?: EMPTY_STRING
        }
        val contentFileBuilder = StringBuilder()
        savedFilePath?.let {
            readFromFile(it).forEach {
                contentFileBuilder.append(it).append(LINE_BREAK)
            }
            binding?.editFileEditText?.setText(contentFileBuilder.toString())
        } ?: run {
            isNewFile = true
        }
    }

    private fun writeToFile(value: String) {

        countFileRepeat = 0
        var fileContent = value

        if (fileContent.isNotBlank()) {
            val fileName = clearFileNameFromBadSymbols(createFileName(value))
            savedFilePath?.let {
                val fileForOperations = File(it)
                val dirToFile = fileForOperations.parent!!
                val oldFileName = fileForOperations.name
                val currentFileName = buildFileName(
                    fileName,
                    dirToFile,
                    oldFileName
                )

                if (newFileNameForRepeatedFileName.isNotBlank()) {
                    fileContent =
                        StringBuilder(fileContent).insert(0, fileName)
                            .insert(fileName.length, LINE_BREAK)
                            .toString()
                }

                File(
                    currentFileName
                ).bufferedWriter(charset = charset(CHAR_SET))
                    .use {
                        it.write(fileContent)
                    }
            } ?: run {
                val pathToNewFile = buildPathForNewFile(fileName)

                if (newFileNameForRepeatedFileName.isNotBlank()) {
                    fileContent =
                        StringBuilder(fileContent).insert(
                            0, File(pathToNewFile).nameWithoutExtension
                        ).insert(File(pathToNewFile).nameWithoutExtension.length, LINE_BREAK)
                            .toString()
                }
                File(pathToNewFile).createNewFile()
                File(pathToNewFile).bufferedWriter(charset = charset(CHAR_SET)).use {
                    it.write(fileContent)
                }
            }
        }
    }

    private fun readFromFile(filePath: String) = File(filePath).bufferedReader().readLines()

    private fun buildPathForNewFile(fileName: String): String {
        val pathToNoteBookDir = NotebookApp.INSTANCE!!.getNotebookDir()
        findFileWithTheSameName(fileName, pathToNoteBookDir)
        val newFileName: String = if (newFileNameForRepeatedFileName.isNotBlank()) {
            newFileNameForRepeatedFileName
        } else {
            fileName
        }

        return StringBuilder()
            .append(pathToNoteBookDir)
            .append(File.separator)
            .append(newFileName)
            .append(FILE_EXT).toString()
    }

    private fun createFileName(value: String): String {
        return when {
            value.contains(LINE_BREAK) ->
                if (value.first().toString() == LINE_BREAK) {
                    firstLineFileIsEmpty = true
                    DEFAULT_FILE_NAME
                } else
                    if (value.length <= MAX_FILE_NAME_SIZE
                        || value.indexOf(LINE_BREAK) < MAX_FILE_NAME_SIZE
                    ) {
                        value.substring(START_STRING_INDEX, value.indexOf(LINE_BREAK)).trim()
                    } else {
                        value.substring(START_STRING_INDEX, MAX_FILE_NAME_SIZE).trim()
                    }
            value.length <= MAX_FILE_NAME_SIZE -> value.trim()
            value.length >= MAX_FILE_NAME_SIZE -> value.substring(
                START_STRING_INDEX,
                MAX_FILE_NAME_SIZE
            ).trim()
            else -> {
                EMPTY_STRING
            }
        }
    }

    private fun clearFileNameFromBadSymbols(fileName: String) =
        fileName.replace(Regex(FILE_NAME_REGEX), EMPTY_STRING)


    private fun findFileWithTheSameName(fileName: String, dir: String) {
        File(dir).listFiles()!!.forEachIndexed { _, file ->

            if (file.nameWithoutExtension == fileName) {
                countFileRepeat++
                createNewNameForRepeatFile(fileName, countFileRepeat, dir)
            }
        }
    }

    private fun createNewNameForRepeatFile(
        fileName: String,
        index: Int,
        dir: String
    ) {
        val originallyName: String = if (index > 1) {
            newFileNameForRepeatedFileName.substring(
                START_STRING_INDEX, newFileNameForRepeatedFileName.indexOf(
                    EMPTY_SPACE
                )
            )
        } else {
            fileName
        }
        newFileNameForRepeatedFileName =
            StringBuilder(originallyName)
                .append(EMPTY_SPACE)
                .append(OPEN_BRACKET)
                .append(index)
                .append(CLOSE_BRACKET)
                .toString()
        findFileWithTheSameName(newFileNameForRepeatedFileName, dir)
    }

    private fun buildFileName(fileName: String, dirToFile: String, oldFileName: String): String {
        val newFileNameBuilder =
            StringBuilder(fileName)
                .append(FILE_EXT).toString()
        File(dirToFile, oldFileName).renameTo(File(dirToFile, newFileNameBuilder))
        return StringBuilder(dirToFile).append(File.separator)
            .append(newFileNameBuilder).toString()
    }

    private fun setContentFromFileToFragment(path: String) {
        val contentFileBuilder = StringBuilder()
        if (path.isNotBlank()) {
            readFromFile(path).forEach {
                contentFileBuilder.append(it).append(LINE_BREAK)
            }
        }
        binding?.editFileEditText?.setText(contentFileBuilder.toString())
    }

    override fun onPause() {
        activity?.let {
            if (resources.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                it.supportFragmentManager.beginTransaction().remove(this).commit()
            }
        }
        writeToFile(binding?.editFileEditText?.text.toString())

        super.onPause()
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }

    override fun setData(data: String?) {

        if (data == null) {
            savedFilePath = null
            setContentFromFileToFragment(EMPTY_STRING)
        } else {
            savedFilePath = data
            setContentFromFileToFragment(data)
        }
    }
}

interface SetDataContract {
    fun setData(data: String?)
}

