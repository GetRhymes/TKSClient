package com.poly.client

import com.poly.client.util.SERVER_HOST
import com.poly.client.util.SERVER_PORT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Thread.currentThread
import java.util.*


class Application {
    fun startApplication() {
        println("Write your name:")
        val scanner = Scanner(System.`in`)
        MessageData.userName = scanner.nextLine()

        runBlocking {
            launch(Dispatchers.IO) {
                Client.startClient(SERVER_HOST, SERVER_PORT)
            }
            launch(Dispatchers.IO) {
                while (!currentThread().isInterrupted) {
                    Buffer.senderBuffer.add(MessageData.createMessage(scanner.nextLine()))
                }
            }
        }
    }
}