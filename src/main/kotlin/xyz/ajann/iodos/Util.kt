package xyz.ajann.iodos

import ch.qos.logback.classic.Level
import org.slf4j.LoggerFactory
import org.springframework.core.io.DefaultResourceLoader
import java.io.*
import org.apache.commons.io.FileUtils
import java.io.IOException



val resourceLoader = DefaultResourceLoader()

fun getConfigurationDirectory(): String {
    val useSystemDirs = System.getenv("IODOS_USE_SYSTEM_DIRS").toBoolean()
    val os = System.getProperty("os.name").toLowerCase()

    if(useSystemDirs) {
        if(os.contains("windows")) {
            return "C:\\Iodos-Server"
        } else if(os.contains("linux")) {
            return "/etc/iodos-server"
        }
    }

    return System.getProperty("user.dir")
}

fun setMongoLogLevels() {
    if(!System.getenv("NECTAR_MONGO_DEBUG").toBoolean()) {
        (LoggerFactory.getLogger("org.mongodb.driver.protocol.command") as ch.qos.logback.classic.Logger).level = Level.OFF
        (LoggerFactory.getLogger("org.mongodb.driver.cluster") as ch.qos.logback.classic.Logger).level = Level.OFF
    }
}

/**
 * Copies a "resource" file from the JAR or resource folder to
 * the filesystem.
 * @param resource The resource file name.
 * @param copyLocation The location to copy the resource to on the
 * filesystem.
 * @throws IOException If there is an exception while attempting to
 * copy the file.
 */
@Throws(IOException::class)
fun copyResourceTo(resource: String, copyLocation: File) {
    val in1 = resourceLoader.getResource(resource).inputStream
    FileUtils.copyInputStreamToFile(in1, copyLocation)
}

/**
 * Read the full contents of a "resource" file as
 * a String.
 * @param resource The name of the resource file.
 * @return The full contents of the resource file as a String.
 */
@Throws(IOException::class)
fun getResourceContents(resource: String): String {
    return getContents(resourceLoader.getResource(resource).inputStream)
}

/**
 * Read the full contents of a file on the filesystem.
 */
@Throws(IOException::class)
fun getFileContents(file: File): String {
    return getContents(FileInputStream(file))
}

fun getContents(in1: InputStream): String {
    val reader = BufferedReader(InputStreamReader(in1))

    var line: String?
    val sb = StringBuilder()
    while (true) {
        line = reader.readLine()
        if(line == null) break

        sb.append(line).append("\n")
    }

    reader.close()

    return sb.toString()
}