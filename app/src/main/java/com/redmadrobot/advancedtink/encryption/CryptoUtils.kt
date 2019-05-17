package com.redmadrobot.advancedtink.encryption

import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object CryptoUtils {
    fun sha256(text: String, encodedBase64: Boolean = false): String {
        val hashBytes = sha256(text)

        return if (encodedBase64) {
            Base64.encodeToString(hashBytes, Base64.NO_WRAP)
        } else {
            hashBytes.map { String.format("%02x", it) }.reduce { acc, s -> acc + s }
        }
    }

    fun sha256(text: String) = sha256(text.toByteArray())

    fun sha256(byteArray: ByteArray): ByteArray {
        val digest = try {
            MessageDigest.getInstance("SHA-256")
        } catch (e: NoSuchAlgorithmException) {
            MessageDigest.getInstance("SHA")
        }

        return with(digest) {
            update(byteArray)
            digest()
        }
    }

    fun base64Encode(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun base64Decode(string: String): ByteArray {
        return Base64.decode(string, Base64.DEFAULT)
    }
}