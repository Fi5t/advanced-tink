package com.redmadrobot.advancedtink.extension

import com.ironz.binaryprefs.Preferences
import com.ironz.binaryprefs.PreferencesEditor


inline fun Preferences.modify(commit: Boolean = false, action: PreferencesEditor.() -> Unit) {
    val editor = edit()

    action(editor)

    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}