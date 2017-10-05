package xyz.ajann.iodos

import org.ini4j.Ini
import java.io.File
import java.io.IOException

@Throws(IOException::class)
fun loadConfiguration(configLocation: File): IodosServerConfiguration {
    val ini = Ini()
    ini.load(configLocation)

    return IodosServerConfiguration(loadConfigNetwork(ini), loadConfigDB(ini))
}

private fun loadConfigNetwork(ini: Ini): NetworkConfiguration {
    val bindPort = ini.get("network")?.get("bindPort")?.toInt()
            ?: throw RuntimeException("Failed to find \"bindPort\" value in section [network]")

    return NetworkConfiguration(bindPort)
}

private fun loadConfigDB(ini: Ini): DBConfiguration {
    val ip = ini.get("db")?.get("ip")
            ?: throw RuntimeException("Failed to find \"ip\" value in section [db]")
    val port = ini.get("db")?.get("port")?.toInt()
            ?: throw RuntimeException("Failed to find \"port\" value in section [db]")
    val name = ini.get("db")?.get("name")
            ?: throw RuntimeException("Failed to find \"name\" value in section [db]")

    return DBConfiguration(ip, port, name)
}

/**
 * Represents the Iodos Server Configuration file, as a class.
 */
data class IodosServerConfiguration (
    val network: NetworkConfiguration,
    val db: DBConfiguration
)

/**
 * The Network configuration section of the config file.
 */
data class NetworkConfiguration (
        val bindPort: Int
)

/**
 * The Database (db) configuration section of the config file.
 */
data class DBConfiguration (
        val ip: String,
        val port: Int,
        val name: String
)