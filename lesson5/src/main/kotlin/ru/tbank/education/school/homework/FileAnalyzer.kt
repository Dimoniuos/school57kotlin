package ru.tbank.education.school.homework

import java.io.BufferedReader

import java.io.EOFException
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * Интерфейс для подсчёта строк и слов в файле.
 */
interface FileAnalyzer {

    /**
     * Считает количество строк и слов в указанном входном файле и записывает результат в выходной файл.
     *
     * Словом считается последовательность символов, разделённая пробелами,
     * табуляциями или знаками перевода строки. Пустые части после разделения не считаются словами.
     *
     * @param inputFilePath путь к входному текстовому файлу
     * @param outputFilePath путь к выходному файлу, в который будет записан результат
     * @return true если операция успешна, иначе false
     */
    fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean
}

class IOFileAnalyzer : FileAnalyzer {
    override fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean {
        var lines = 0
        var words = 0
        try {
            val reader = BufferedReader(FileReader(inputFilePath))
            reader.use {
                val Lines = reader.readLines()
                lines = Lines.size
                words = Lines.sumOf { it.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size }
            }
        } catch (e: FileNotFoundException) {
            println("Файл не найден")
            return false
        } catch (e: EOFException) {
            println("Неожиданное окончание файла")
            return false
        } catch (e: IOException) {
            println("Неизвестная ошибка")
            return false
        }

        try {
            FileWriter(outputFilePath).use { writer ->
                writer.write("Общее количество строк: $lines\n")
                writer.write("Общее количество слов: $words\n")
            }
        } catch (e: IOException) {
            println("Ошибка записи")
            return false
        } catch (e: Exception) {
            println("Произошла ошибка: ${e.message}")
        }
        return true

    }
}

class NIOFileAnalyzer : FileAnalyzer {
    override fun countLinesAndWordsInFile(inputFilePath: String, outputFilePath: String): Boolean {
        try {
            val lines = Files.readAllLines(Paths.get(inputFilePath))
            val line = lines.size
            val words = lines.sumOf { line ->
                line.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
            }
            Files.write(
                Paths.get(outputFilePath),
                listOf(
                    "Общее количество строк: $line",
                    "Общее количество слов: $words"
                ),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        } catch (e: IOException) {
            println("Ошибка с файлом")
            return false
        } catch (e: Exception) {
            println("Произошла ошибка: ${e.message}")
            return false
        }
        return true
    }
}
