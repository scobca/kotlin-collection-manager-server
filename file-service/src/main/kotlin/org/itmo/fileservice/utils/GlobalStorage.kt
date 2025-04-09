package org.itmo.fileservice.utils

import java.io.File

object GlobalStorage {
    private var databaseFilename: String = ""
    private var lastSaveIndex: Long = 0

    fun setDatabaseFilename(name: String) {
        databaseFilename = if (name.endsWith(".json")) name else "$name.json"
        setLastSaveIndex()
    }

    private fun setLastSaveIndex() {
        if (databaseFilename.contains(Regex("_version*"))) {
            val index = databaseFilename.split("_").last().substring(7).toLong()
            lastSaveIndex = index
        } else lastSaveIndex = 0
    }

    fun getNewSaveFilename(): String {
        lastSaveIndex++

        val fileExtension = databaseFilename.split(".").last()
        return databaseFilename.split(".").dropLast(1).joinToString(".") + "_version$lastSaveIndex" + ".$fileExtension"
    }

    fun getDatabaseFilename(): String {
        return databaseFilename
    }

    fun getLastSaveIndex(): Long {
        return lastSaveIndex
    }
}