package com.poly.client

import com.poly.models.Message
import java.util.*


fun main(args: Array<String>) {
    println("Write your name:")
    val scanner = Scanner(System.`in`)
    MessageData.userName = scanner.nextLine()

    Thread {
        Client.startClient("78.37.108.101", 65432)
    }.start()

    Thread {
        Client.readMessage()
    }.start()

    while (true) {
        Buffer.senderBuffer.add(MessageData.createMessage(scanner.nextLine()))
    }
}

object Buffer {
    val receiverBuffer = LinkedList<Pair<Message, ByteArray?>>()
    val senderBuffer = LinkedList<Pair<Message, ByteArray?>>()
}

