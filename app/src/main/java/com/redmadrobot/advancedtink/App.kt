package com.redmadrobot.advancedtink

import android.app.Application
import com.google.crypto.tink.aead.AeadFactory
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.daead.DeterministicAeadFactory
import com.google.crypto.tink.daead.DeterministicAeadKeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.ironz.binaryprefs.BinaryPreferencesBuilder
import com.redmadrobot.advancedtink.encryption.TinkKeyEncryption
import com.redmadrobot.advancedtink.encryption.TinkValueEncryption
import java.security.GeneralSecurityException


class App : Application() {
    companion object {
        private const val KEYSET_NAME = "master_keyset"
        private const val PREFERENCE_FILE = "master_key_preference"
        private const val MASTER_KEY_URI = "android-keystore://master_key"

        private const val DKEYSET_NAME = "dmaster_keyset"
        private const val DPREFERENCE_FILE = "dmaster_key_preference"
        private const val DMASTER_KEY_URI = "android-keystore://dmaster_key"
    }

    //region Use DI, please...

    val aead by lazy {
        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(this, KEYSET_NAME, PREFERENCE_FILE)
            .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle

        AeadFactory.getPrimitive(keysetHandle)
    }

    val daead by lazy {
        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(this, DKEYSET_NAME, DPREFERENCE_FILE)
            .withKeyTemplate(DeterministicAeadKeyTemplates.AES256_SIV)
            .withMasterKeyUri(DMASTER_KEY_URI)
            .build()
            .keysetHandle

        DeterministicAeadFactory.getPrimitive(keysetHandle)
    }

    val preferences by lazy {
        BinaryPreferencesBuilder(this)
            .keyEncryption(TinkKeyEncryption(this, daead))
            .valueEncryption(TinkValueEncryption(this, aead))
            .build()
    }

    //endregion

    override fun onCreate() {
        super.onCreate()

        initTink()
    }

    private fun initTink() {
        try {
            TinkConfig.register()
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        }
    }
}