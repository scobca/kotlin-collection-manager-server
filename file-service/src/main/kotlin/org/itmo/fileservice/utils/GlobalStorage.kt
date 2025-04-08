package org.itmo.fileservice.utils

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
        val newIndex = getLastSaveIndex().toLong() + 1
        lastSaveIndex++

        return databaseFilename + "_" + newIndex
    }

    fun getDatabaseFilename(): String {
        return databaseFilename
    }
    fun getLastSaveIndex(): Int {
        return lastSaveIndex.toInt()
    }
}