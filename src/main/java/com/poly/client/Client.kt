package com.poly.client

import com.poly.client.Buffer.receiverBuffer
import com.poly.client.Buffer.senderBuffer
import com.poly.client.MessageData.userName
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
                    val messageWithContent = senderBuffer.poll()
                    sender.write(messageWithContent)
                } else if (receiver.readyForMessageReading()) {
                    val message = receiver.read()
                    receiverBuffer.add(message)
                }
            } else {
                exitCondition = true
                println("$ERROR $CONNECTION_LOST")
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
                val messageWithContent = receiverBuffer.poll()
                var fileBlock = VOID
                val message = messageWithContent.message
                val content: ByteArray? = messageWithContent.content
                if (content != null) {
                    fileBlock = "$ATTACHMENT ${writeNewFile(message.fileName, content)}"
                }
                println("[${message.message}][${message.name}]: ${message.message} $fileBlock")
            }
        }
    }

    private fun createDir(): String {
        val directory = File(
            System.getProperty(USER_HOME) +
                    File.separator + DESKTOP +
                    File.separator + userName
        )
        if (!directory.exists()) directory.mkdir()
        return directory.absolutePath
    }

    private fun writeNewFile(fileName: String, fileContent: ByteArray): String {
        val resultFile = File("${createDir()}${File.separator}$fileName")
        resultFile.createNewFile()
        val fos = FileOutputStream(resultFile)
        fos.write(fileContent)
        fos.close()
        return resultFile.absolutePath
    }
}
