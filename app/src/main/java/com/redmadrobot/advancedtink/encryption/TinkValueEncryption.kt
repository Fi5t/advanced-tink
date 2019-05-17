package com.redmadrobot.advancedtink.encryption

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.Aead
import com.ironz.binaryprefs.encryption.ValueEncryption


class TinkValueEncryption(context: Context, private val aead: Aead) : ValueEncryption {
    private val signature = SysUtils.getSignatureSha(context).takeLast(16).toByteArray()

    override fun encrypt(plaintext: ByteArray): ByteArray {
        val ciphertext = aead.encrypt(plaintext, signature)
        return Base64.encode(ciphertext, Base64.DEFAULT)
    }

    override fun decrypt(cipher: ByteArray): ByteArray {
        val cipertext = Base64.decode(cipher, Base64.DEFAULT)
        return aead.decrypt(cipertext, signature)
    }
}