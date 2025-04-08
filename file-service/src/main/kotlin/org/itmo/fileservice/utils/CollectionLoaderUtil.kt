package org.itmo.fileservice.utils

import org.itmo.fileservice.collection.Collection
import org.itmo.fileservice.parser.FlatParser
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.IOException

@Service
class CollectionLoaderUtil(private val collection: Collection, private val parser: FlatParser) {
    fun getCollectionFromFile() {
        val filename = GlobalStorage.getDatabaseFilename()
        val resource = File(filename)

        if (resource.exists()) {
            if (resource.length() > 0) {
                val inputStream = FileInputStream(resource)
                parser.parseFromJson(inputStream)
            }
        } else createNewFile()
    }

    private fun createNewFile() {
        val filename = GlobalStorage.getDatabaseFilename()
        val file = File(filename)

        try {
            if (file.createNewFile()) {
                println("Файл $filename создан успешно.")
            } else {
                println("Ошибка при создании файла $filename")
            }
        } catch (e: IOException) {
            println("Ошибка при создании файла: $e")
        }
    }
}