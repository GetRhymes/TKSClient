package com.poly.client

import com.poly.client.Buffer.receiverBuffer
import com.poly.client.Buffer.senderBuffer
import com.poly.client.MessageData.userName
import com.poly.models.MessageWithContent
import com.poly.sockets.MessageReader
import com.poly.sockets.MessageWriter
import java.io.File
import java.io.FileOutputStream
import java.lang.Thread.sleep
import java.net.Socket
import java.net.SocketException

object Client {
    fun startClient(serverHost: String, serverPort: Int) {
        val socket = Socket(serverHost, serverPort)

        val sender = MessageWriter(socket.getOutputStream())
        val receiver = MessageReader(socket.getInputStream())

        var exitCondition = false

        while (!exitCondition) {
            if (socket.isConnected) {
                if (senderBuffer.size > 0) {
                    val (message, fileContent) = senderBuffer.poll()
                    sender.write(MessageWithContent(message, fileContent))
                } else if (receiver.readyForMessageReading()) {
                    val message = receiver.read()
                    receiverBuffer.add(message.message to message.content)
                }
            } else {
                exitCondition = true
            }
        }
        try {
            socket.close()
        } catch (e: SocketException) {
            e.printStackTrace()
        }
    }

    fun readMessage() {
        while (true) {
            sleep(1)
            if (receiverBuffer.size > 0) {
                val (message, fileContent) = receiverBuffer.poll()
                var fileBlock = ""
                if (!message.fileName.isNullOrEmpty() && fileContent != null) {
                    fileBlock = "[Attachment] ${writeNewFile(message.fileName, fileContent)}"
                }
                println("[${message.date}][${message.name}]: ${message.message} $fileBlock")
            }
        }
    }

    private fun createDir(): String {
        val directory = File(System.getProperty("user.home") + "/Desktop/${userName}_output")
        if (!directory.exists()) directory.mkdir()
        return directory.absolutePath
    }

    private fun writeNewFile(fileName: String, fileContent: ByteArray): String {
        val resultFile = File("${createDir()}/$fileName")
        resultFile.createNewFile()
        val fos = FileOutputStream(resultFile)
        fos.write(fileContent)
        fos.close()
        return resultFile.absolutePath
    }
}
//privet fp:-/ /Users/a19501710/StudProject/TKSClient/pom.xml