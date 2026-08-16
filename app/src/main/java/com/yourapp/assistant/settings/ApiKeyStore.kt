package com.yourapp.assistant.settings

import android.content.Context

object ApiKeyStore {

    private const val PREFS = "assistant_settings"
    private const val KEY_GEMINI = "gemini_api_key"

    fun save(context: Context, apiKey: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_GEMINI, apiKey.trim())
            .apply()
    }

    fun get(context: Context): String {
        return context
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_GEMINI, "")
            .orEmpty()
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_GEMINI)
            .apply()
    }
}
