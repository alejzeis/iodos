package xyz.ajann.iodos

import org.apache.commons.codec.digest.DigestUtils
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.util.io.pem.PemReader
import java.io.FileReader
import java.lang.Exception
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

/**
 * Compute the SHA-512 Hash of a String.
 * @param plaintext The raw text to be hashed.
 * @return The has of the provided text.
 */
fun computeSHA512(plaintext: String): String {
    return DigestUtils.sha512Hex(plaintext)
}

@Throws(Exception::class)
fun loadPrivateKeyFromDisk(location: String): ECPrivateKey {
    val pem = PemReader(FileReader(location)).readPemObject()
    return KeyFactory.getInstance("EC","BC").generatePrivate(PKCS8EncodedKeySpec(pem.content)) as ECPrivateKey
}

@Throws(Exception::class)
fun loadPublicKeyFromDisk(location: String): ECPublicKey {
    val pem = PemReader(FileReader(location)).readPemObject()
    return KeyFactory.getInstance("EC","BC").generatePublic(X509EncodedKeySpec(pem.content)) as ECPublicKey
}

fun generateECKeyPair(): KeyPair {
    val spec = ECNamedCurveTable.getParameterSpec("secp384r1")
    val generator = KeyPairGenerator.getInstance("ECDSA", "BC")
    return generator.genKeyPair()
}