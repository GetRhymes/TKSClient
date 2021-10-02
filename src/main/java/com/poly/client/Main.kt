package com.poly.client

import com.poly.models.MessageWithContent
import java.util.*


fun main() {
    println("Write your name:")
    val scanner = Scanner(System.`in`)
    MessageData.userName = scanner.nextLine()

    Thread {
        Client.startClient(SERVER_HOST, SERVER_PORT)
    }.start()

    Thread {
        Client.readMessage()
    }.start()

    while (true) {
        Buffer.senderBuffer.add(MessageData.createMessage(scanner.nextLine()))
    }
}

object Buffer {
    val receiverBuffer = LinkedList<MessageWithContent>()
    val senderBuffer = LinkedList<MessageWithContent>()
}
