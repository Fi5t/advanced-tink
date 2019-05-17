package com.redmadrobot.advancedtink.encryption

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build


object SysUtils {
    @Suppress("DEPRECATION")
    @TargetApi(Build.VERSION_CODES.P)
    @SuppressLint("WrongConstant", "PackageManagerGetSignatures")
    fun getSignatureSha(context: Context): ByteArray {
        val signatures = with(context.packageManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                    .signingInfo
                    .apkContentsSigners
            } else {
                getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES).signatures
            }
        }


        return CryptoUtils.sha256(signatures.first().toByteArray())
    }
}