package com.stash.inbox.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class Note(val id: String, val content: String, val createdAt: Long)

/** Local prototype storage. This is not encrypted and must not hold sensitive data. */
class NoteStore(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences("stash_notes", Context.MODE_PRIVATE)

    fun load(): List<Note> {
        val json = JSONArray(preferences.getString("notes", "[]"))
        return (0 until json.length()).map { index ->
            val item = json.getJSONObject(index)
            Note(item.getString("id"), item.getString("content"), item.getLong("createdAt"))
        }.sortedByDescending { it.createdAt }
    }

    fun newNote(content: String) = Note(UUID.randomUUID().toString(), content, System.currentTimeMillis())

    fun save(notes: List<Note>) {
        val json = JSONArray()
        notes.forEach { note ->
            json.put(JSONObject().put("id", note.id).put("content", note.content).put("createdAt", note.createdAt))
        }
        preferences.edit().putString("notes", json.toString()).apply()
    }
}
