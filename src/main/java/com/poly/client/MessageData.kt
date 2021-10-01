package com.poly.client

import com.poly.models.Message
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object MessageData {

    var userName = ""

    private class PolyFile(val fileName: String? = null, val fileSize: Int? = null, val fileContent: ByteArray? = null)

    private fun getPolyFile(path: String): PolyFile {
        val fileName = File(path).name
        val fileContent = Files.readAllBytes(Paths.get(path))
        val fileSize = fileContent.size
        return PolyFile(fileName, fileSize, fileContent)
    }

    fun createMessage(message: String): Pair<Message, ByteArray?> {
        val partsOfMessage = message.split("fp:-/")
        val polyFile = if (partsOfMessage.size > 1) getPolyFile(partsOfMessage[1].trim()) else PolyFile()
        return Message(null, userName, partsOfMessage[0], polyFile.fileName, polyFile.fileSize) to polyFile.fileContent
    }
}
