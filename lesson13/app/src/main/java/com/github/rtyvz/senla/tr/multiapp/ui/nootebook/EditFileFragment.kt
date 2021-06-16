package com.github.rtyvz.senla.tr.multiapp.ui.nootebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.MultiFuncApp
import com.github.rtyvz.senla.tr.multiapp.databinding.EditFileFragmentBinding
import java.io.File

class EditFileFragment : Fragment() {
    private var binding: EditFileFragmentBinding? = null
    private var savedFilePath: String? = null
    private var newFileNameForRepeatedFileName: String = EMPTY_STRING
    private var repeatedFileName = EMPTY_STRING
    private val fileNameRegex = "[\\\\/<>]".toRegex()
    private val countStartValue = 0
    private val valueForStartCheckRepeatName = 1

    companion object {
        private const val CHAR_SET = "UTF-8"
        private const val EMPTY_STRING = ""
        private const val START_STRING_INDEX = 0
        private const val FILE_EXT = ".txt"
        private const val LINE_BREAK = "\n"
        private const val DEFAULT_FILE_NAME = "Документ"
        private const val OPEN_BRACKET = "("
        private const val CLOSE_BRACKET = ")"
        private const val SPACE = " "
        const val EXTRA_FILE_PATH = "FILE_PATH"
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

        getFilePathFromArguments()

        if (savedFilePath != null) {
            setDataToEditText(readFromFile(savedFilePath.toString()))
        }
    }

    private fun setDataToEditText(text: String) {
        binding?.editFileEditText?.setText(text)
    }

    private fun getFilePathFromArguments() {
        arguments?.let {
            savedFilePath = it.getString(EXTRA_FILE_PATH)
        }
    }

    private fun createFile(value: String?): File? {
        var fileName = if (value == null) {
            DEFAULT_FILE_NAME
        } else {
            clearFileName(getFileNameFromContent(value))
        }

        MultiFuncApp.INSTANCE?.let {
            fileName =
                findFileWithTheSameName(fileName, File(it.getNotebookDir()))
        }
        val file = File(buildPathForNewFile(fileName))

        return if (file.createNewFile()) {
            file
        } else {
            null
        }
    }

    private fun clearFileName(fileName: String): String =
        fileName.replace(fileNameRegex, EMPTY_STRING)

    private fun getFileNameFromContent(content: String): String {
        return if (content.contains(LINE_BREAK)) {
            clearFileName(content.substring(START_STRING_INDEX, content.indexOf(LINE_BREAK)))
        } else {
            clearFileName(content)
        }
    }

    private fun insertFileNameIntoFileContent(file: File, fileName: String) {
        val newContent =
            StringBuilder(
                getFileNameFromContent(fileName)
            )
                .append(LINE_BREAK)
                .append(
                    file
                        .readText(charset = charset(CHAR_SET))
                        .substringAfter(LINE_BREAK)
                )
        writeToFile(file, newContent.toString())
    }

    private fun writeToFile(value: String) {
        if (savedFilePath.isNullOrBlank()) {
            when {
                value.startsWith(LINE_BREAK) || value.isBlank() || (value.trim() == EMPTY_STRING) -> {
                    createFile(null)?.let {
                        writeToFile(it, value)
                        insertFileNameIntoFileContent(it, it.nameWithoutExtension)
                    }
                }
                else -> {
                    createFile(value)?.let {
                        writeToFile(it, value)
                    }
                }
            }
        } else {
            savedFilePath?.let {
                val currentFile = File(it)

                if (isFileNamesTheSame(value, currentFile)) {
                    writeToFile(currentFile, value)
                } else {
                    val file = renameFile(value, currentFile)
                    if (file == null) {
                        //если переименовываем файл и получаем такое же имя которое
                        // уже есть в каталоге создаем новый файл что бы не потерять данные
                        createFile(value)?.let { createdFile ->
                            writeToFile(createdFile, value)
                            insertFileNameIntoFileContent(
                                createdFile,
                                createdFile.nameWithoutExtension
                            )
                        }
                    } else {
                        insertFileNameIntoFileContent(file, value)
                    }
                }
            }
        }
    }

    private fun isFileNamesTheSame(text: String, file: File) =
        file.nameWithoutExtension == getFileNameFromContent(text)

    private fun renameFile(text: String, currenFile: File): File? {
        val firstLineInText = getFileNameFromContent(text)
        currenFile.parent?.let {
            val newFileName = findFileWithTheSameName(firstLineInText, File(it))
            if (newFileName.isBlank()) {
                val fileNameForRename =
                    File(
                        currenFile.parent,
                        StringBuilder(firstLineInText).append(FILE_EXT).toString()
                    )
                currenFile.renameTo(fileNameForRename)
                return fileNameForRename
            }
        }
        return null
    }

    private fun writeToFile(file: File, value: String) {
        file.bufferedWriter(charset = charset(CHAR_SET)).use { writer ->
            writer.write(value)
        }
    }

    private fun readFromFile(filePath: String?): String {
        return if (filePath != null) {
            File(filePath).bufferedReader().readText()
        } else {
            EMPTY_STRING
        }
    }

    private fun buildPathForNewFile(fileName: String): String {
        val pathToNoteBookDir = MultiFuncApp.INSTANCE?.getNotebookDir()
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

    private fun findFileWithTheSameName(fileName: String, dir: File): String {
        repeatedFileName = fileName
        var repeatFileCount = countStartValue

        if (dir.listFiles().isNullOrEmpty()) {
            return fileName
        } else {
            dir.listFiles()?.forEachIndexed { _, file ->
                if (file.nameWithoutExtension == repeatedFileName) {
                    repeatFileCount++
                    createNewNameForRepeatFile(repeatedFileName, repeatFileCount)
                }
            }
        }

        return repeatedFileName
    }

    private fun createNewNameForRepeatFile(
        fileName: String,
        index: Int
    ) {
        var newFileName: String = fileName

        if (index > valueForStartCheckRepeatName) {
            newFileName = repeatedFileName.substring(
                START_STRING_INDEX, repeatedFileName.indexOf(
                    SPACE
                )
            )
        }

        repeatedFileName = StringBuilder(newFileName)
            .append(SPACE)
            .append(OPEN_BRACKET)
            .append(index)
            .append(CLOSE_BRACKET)
            .toString()

    }

    override fun onPause() {
        writeToFile(binding?.editFileEditText?.text.toString())

        super.onPause()
    }

    override fun onDestroy() {
        binding = null

        super.onDestroy()
    }

    fun setPath(path: String?) {
        savedFilePath = path
        setDataToEditText(readFromFile(path))
    }
}