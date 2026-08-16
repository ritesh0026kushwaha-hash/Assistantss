package com.yourapp.assistant.ai

import android.content.Context
import com.yourapp.assistant.settings.ApiKeyStore
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class GeminiClient(private val context: Context) {

    private val client = OkHttpClient()

    fun generate(prompt: String): String {

        val apiKey = ApiKeyStore.get(context)

        if (apiKey.isBlank()) {
            return "Pehle Settings me Gemini API key add karo."
        }

        val url =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"

        val bodyJson = JSONObject()
            .put(
                "contents",
                org.json.JSONArray()
                    .put(
                        JSONObject()
                            .put(
                                "parts",
                                org.json.JSONArray()
                                    .put(
                                        JSONObject().put("text", prompt)
                                    )
                            )
                    )
            )

        val body = bodyJson.toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        return try {
            client.newCall(request).execute().use { response ->

                val responseText = response.body?.string().orEmpty()

                if (!response.isSuccessful) {
                    return "Gemini API error: ${response.code}"
                }

                val json = JSONObject(responseText)

                json.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
            }

        } catch (e: Exception) {
            "API request failed: ${e.message ?: "unknown error"}"
        }
    }
}
