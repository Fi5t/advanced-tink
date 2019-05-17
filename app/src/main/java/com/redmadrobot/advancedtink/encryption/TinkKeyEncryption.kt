package com.redmadrobot.advancedtink.encryption

import android.content.Context
import android.util.Base64
import com.google.crypto.tink.DeterministicAead
import com.ironz.binaryprefs.encryption.KeyEncryption


class TinkKeyEncryption(context: Context, private val aead: DeterministicAead) : KeyEncryption {
    private val signature = SysUtils.getSignatureSha(context)
    private val encoderFlags = Base64.NO_WRAP or Base64.URL_SAFE

    override fun encrypt(plaintext: String): String {
        val ciphertext = aead.encryptDeterministically(plaintext.toByteArray(), signature)
        return Base64.encodeToString(ciphertext, encoderFlags)
    }

    override fun decrypt(cipher: String): String {
        val cipertext = Base64.decode(cipher, encoderFlags)
        return String(aead.decryptDeterministically(cipertext, signature))
    }
}