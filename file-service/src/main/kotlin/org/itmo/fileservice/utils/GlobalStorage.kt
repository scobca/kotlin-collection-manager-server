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
        val baseName = databaseFilename.removeSuffix(".json")
        val directory = File(".")

        if (!directory.exists() || !directory.isDirectory) {
            lastSaveIndex = 0
            return
        }

        val regex = Regex(Regex.escape(baseName) + "_version(\\d+)\\.json")

        lastSaveIndex = directory.listFiles()
            ?.mapNotNull { file ->
                val match = regex.matchEntire(file.name)
                match?.groupValues?.get(1)?.toLongOrNull()
            }
            ?.maxOrNull() ?: 0

        println("$lastSaveIndex sdlksldkl")
    }

    fun getNewSaveFilename(): String {
        lastSaveIndex++
        val baseName = databaseFilename.removeSuffix(".json")
        return "${baseName}_version${lastSaveIndex}.json"
    }

    fun getDatabaseFilename(): String {
        return databaseFilename
    }

    fun getLastSaveIndex(): Long {
        return lastSaveIndex
    }
}
