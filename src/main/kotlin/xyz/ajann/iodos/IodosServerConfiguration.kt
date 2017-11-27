package xyz.ajann.iodos

import org.ini4j.Ini
import java.io.File
import java.io.IOException
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey

@Throws(IOException::class)
fun loadConfiguration(configLocation: File): IodosServerConfiguration {
    val ini = Ini()
    ini.load(configLocation)

    return IodosServerConfiguration(loadConfigNetwork(ini), loadConfigDB(ini), loadConfigSecurity(ini))
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

@Throws(Exception::class)
private fun loadConfigSecurity(ini: Ini): SecurityConfiguration {
    val privateLocation = ini.get("security")?.get("privateKeyLocation")?.toString()
            ?: throw RuntimeException("Failed to find \"privateKeyLocation\" value in section [security]")
    val publicLocation = ini.get("security")?.get("publicKeyLocation")?.toString()
            ?: throw RuntimeException("Failed to find \"publicKeyLocation\" value in section [security]")

    return SecurityConfiguration(loadPrivateKeyFromDisk(privateLocation), loadPublicKeyFromDisk(publicLocation))
}

/**
 * Represents the Iodos Server Configuration file, as a class.
 */
data class IodosServerConfiguration (
    val network: NetworkConfiguration,
    val db: DBConfiguration,
    val security: SecurityConfiguration
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

/**
 * The Security configuration section of the config file.
 */
data class SecurityConfiguration (
    val privateKey: ECPrivateKey,
    val publicKey:  ECPublicKey
)