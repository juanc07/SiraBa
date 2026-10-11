package net.thinkbloxph.siraba.ui.navigation

import android.content.Context

/** Onboarding completion is device-local, non-sensitive and never a role/identity authority. */
class IntroPreferences(context: Context) {
    private val preferences = context.applicationContext
        .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun hasSeenIntro(): Boolean = preferences.getBoolean(KEY_SEEN, false)

    fun markSeen() {
        preferences.edit().putBoolean(KEY_SEEN, true).apply()
    }

    companion object {
        const val PREF_NAME = "siraba_onboarding"
        private const val KEY_SEEN = "intro_seen_v1"
    }
}
