package com.poly.client

import com.poly.client.util.FILE_POINT
import com.poly.client.util.VOID
import com.poly.models.Message
import com.poly.models.MessageWithContent
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

object MessageData {
    var userName = VOID

    private class PolyFile(val fileName: String? = null, val fileSize: Int? = null, val fileContent: ByteArray? = null)

    private fun getPolyFile(path: String): PolyFile {
        val fileName = File(path).name
        val fileContent = Files.readAllBytes(Paths.get(path))
        val fileSize = fileContent.size
        return PolyFile(fileName, fileSize, fileContent)
    }

    fun createMessage(message: String): MessageWithContent {
        val partsOfMessage = message.split(FILE_POINT)
        val polyFile = if (partsOfMessage.size > 1) getPolyFile(partsOfMessage[1].trim()) else PolyFile()
        return MessageWithContent(
            Message(null, userName, partsOfMessage[0], polyFile.fileName, polyFile.fileSize),
            polyFile.fileContent
        )
    }
}
