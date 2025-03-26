package org.itmo.fileservice

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FileServiceApplication(
) : CommandLineRunner {

    override fun run(vararg args: String?) {}
}

fun main(args: Array<String>) {
    runApplication<FileServiceApplication>(*args)
}
