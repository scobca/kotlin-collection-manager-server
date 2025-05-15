package org.itmo.fileservice

import org.itmo.fileservice.strategies.StartupStrategy
import org.itmo.fileservice.utils.GlobalStorage
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class FileServiceApplication(
    private val startupStrategy: StartupStrategy
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val filename = if (args.isNotEmpty()) args[0].toString().trim() else "default.json"
        GlobalStorage.setDatabaseFilename(filename)

        startupStrategy.applicationStart()
    }
}

fun main(args: Array<String>) {
    runApplication<FileServiceApplication>(*args)
}
