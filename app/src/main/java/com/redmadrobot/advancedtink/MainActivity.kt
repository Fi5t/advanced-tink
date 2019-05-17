package com.redmadrobot.advancedtink

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.redmadrobot.advancedtink.encryption.CryptoUtils
import com.redmadrobot.advancedtink.extension.modify
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val USERNAME_KEY = "USERNAME_KEY"
    }

    private val app by lazy { application as App }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCryptoScenarioViews()
        initPreferenesScenario()
    }

    private fun initCryptoScenarioViews() {
        button_encrypt.setOnClickListener {
            val plainText = "${edit_text_plain.text}"
            val cipherText = app.aead.encrypt(plainText.toByteArray(), null)

            edit_text_cipher.setText(CryptoUtils.base64Encode(cipherText))
        }

        button_decrypt.setOnClickListener {
            val cipherText = CryptoUtils.base64Decode("${edit_text_cipher.text}")
            val plainText = app.aead.decrypt(cipherText, null)

            edit_text_plain.setText(String(plainText))
        }
    }

    private fun initPreferenesScenario() {
        button_save.setOnClickListener {
            app.preferences.modify {
                putString(USERNAME_KEY, "${edit_text_username.text}")
            }
        }

        button_load.setOnClickListener {
            val username = app.preferences.getString(USERNAME_KEY, "no username")
            edit_text_username.setText(username)
        }
    }
}
