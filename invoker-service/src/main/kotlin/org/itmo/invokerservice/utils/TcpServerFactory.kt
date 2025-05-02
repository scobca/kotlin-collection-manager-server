package org.itmo.invokerservice.utils

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.itmo.invokerservice.services.InvokerService
import org.springframework.stereotype.Component
import java.io.PrintWriter
import java.net.ServerSocket

@Component
class TcpServerFactory(private val invokerService: InvokerService) {
    @OptIn(DelicateCoroutinesApi::class)
    fun startTcpServer() {
        GlobalScope.launch(Dispatchers.IO) {
            val server = ServerSocket(6000)
            println("Server is running on port ${server.localPort}")

            while (true) {
                val socket = server.accept()

                val input = socket.getInputStream().bufferedReader()
                val output = PrintWriter(socket.getOutputStream(), true)

                val message = input.readLine()
                val response = invokerService.handleCommand(message)

                output.println(response)

                socket.close()
            }
        }
    }
}