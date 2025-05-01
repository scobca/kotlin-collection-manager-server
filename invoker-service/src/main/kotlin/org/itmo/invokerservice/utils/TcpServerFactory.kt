package org.itmo.invokerservice.utils

import java.io.PrintWriter
import java.net.ServerSocket
import kotlin.concurrent.thread

object TcpServerFactory {
    fun startTcpServer() {
        thread {
            val server = ServerSocket(6000)
            println("Server is running on port ${server.localPort}")

            while (true) {
                val socket = server.accept()

                val input = socket.getInputStream().bufferedReader()
                val output = PrintWriter(socket.getOutputStream(), true)

                val message = input.readLine()
                println("Received: $message")

                output.println("Hello from server")

                socket.close()
            }
        }
    }
}