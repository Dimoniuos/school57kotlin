import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun Archivete(Dir: File, zip: File) {
    FileOutputStream(zip).use { stream ->
        ZipOutputStream(stream).use { zipOut ->
            compress(Dir, Dir, zipOut)
        }
    }
}

private fun compress(
        root: File,
        nowFile: File,
        zipOut: ZipOutputStream,
    ) {
    if (nowFile.isDirectory) {
        nowFile.listFiles()?.forEach { file ->
            compress(root, file, zipOut)
        }
    } else {
        val ext = nowFile.extension.lowercase()
        if (ext != "log" && ext != "txt") return
        val filePath = root.toPath().relativize(nowFile.toPath()).toString()
        println("Добавлено: $filePath (${nowFile.length()} bytes)")
        FileInputStream(nowFile).use { stream ->
            val zipEntry = ZipEntry(filePath)
            zipOut.putNextEntry(zipEntry)
            stream.copyTo(zipOut, bufferSize = 4096)
            zipOut.closeEntry()
        }
    }
}
