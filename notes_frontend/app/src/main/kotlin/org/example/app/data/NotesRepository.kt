package org.example.app.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import org.example.app.models.Note

/**
 * PUBLIC_INTERFACE
 * NotesRepository provides CRUD and search operations for notes stored in SQLite.
 *
 * @constructor Provide a Context to initialize the underlying database helper.
 */
class NotesRepository(context: Context) {

    private val dbHelper = NotesDatabase(context.applicationContext)

    // PUBLIC_INTERFACE
    fun getAllNotes(): List<Note> {
        /** Returns all notes ordered by last updated (descending). */
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            NotesDatabase.TABLE_NOTES,
            PROJECTION,
            null, null, null, null,
            "${NotesDatabase.COL_UPDATED_AT} DESC"
        )
        return cursor.use { mapCursor(it) }
    }

    // PUBLIC_INTERFACE
    fun searchNotes(query: String): List<Note> {
        /** Returns notes whose title or content contains the query (case-insensitive). */
        val db = dbHelper.readableDatabase
        val like = "%$query%"
        val cursor = db.query(
            NotesDatabase.TABLE_NOTES,
            PROJECTION,
            "${NotesDatabase.COL_TITLE} LIKE ? OR ${NotesDatabase.COL_CONTENT} LIKE ?",
            arrayOf(like, like),
            null, null,
            "${NotesDatabase.COL_UPDATED_AT} DESC"
        )
        return cursor.use { mapCursor(it) }
    }

    // PUBLIC_INTERFACE
    fun getNote(id: Long): Note? {
        /** Returns a single note by id or null if not found. */
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            NotesDatabase.TABLE_NOTES,
            PROJECTION,
            "${NotesDatabase.COL_ID}=?",
            arrayOf(id.toString()),
            null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) return readRow(it)
        }
        return null
    }

    // PUBLIC_INTERFACE
    fun insertNote(title: String, content: String): Long {
        /** Inserts a new note and returns its id. */
        val db = dbHelper.writableDatabase
        val now = System.currentTimeMillis()
        val values = ContentValues().apply {
            put(NotesDatabase.COL_TITLE, title)
            put(NotesDatabase.COL_CONTENT, content)
            put(NotesDatabase.COL_UPDATED_AT, now)
        }
        return db.insert(NotesDatabase.TABLE_NOTES, null, values)
    }

    // PUBLIC_INTERFACE
    fun updateNote(id: Long, title: String, content: String): Boolean {
        /** Updates an existing note. Returns true if rows were affected. */
        val db = dbHelper.writableDatabase
        val now = System.currentTimeMillis()
        val values = ContentValues().apply {
            put(NotesDatabase.COL_TITLE, title)
            put(NotesDatabase.COL_CONTENT, content)
            put(NotesDatabase.COL_UPDATED_AT, now)
        }
        val rows = db.update(
            NotesDatabase.TABLE_NOTES,
            values,
            "${NotesDatabase.COL_ID}=?",
            arrayOf(id.toString())
        )
        return rows > 0
    }

    // PUBLIC_INTERFACE
    fun deleteNote(id: Long): Boolean {
        /** Deletes a note by id. Returns true if rows were affected. */
        val db = dbHelper.writableDatabase
        val rows = db.delete(
            NotesDatabase.TABLE_NOTES,
            "${NotesDatabase.COL_ID}=?",
            arrayOf(id.toString())
        )
        return rows > 0
    }

    private fun mapCursor(cursor: Cursor): List<Note> {
        val items = ArrayList<Note>()
        while (cursor.moveToNext()) {
            items.add(readRow(cursor))
        }
        return items
    }

    private fun readRow(cursor: Cursor): Note {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(NotesDatabase.COL_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(NotesDatabase.COL_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(NotesDatabase.COL_CONTENT))
        val updatedAt = cursor.getLong(cursor.getColumnIndexOrThrow(NotesDatabase.COL_UPDATED_AT))
        return Note(id, title, content, updatedAt)
    }

    companion object {
        private val PROJECTION = arrayOf(
            NotesDatabase.COL_ID,
            NotesDatabase.COL_TITLE,
            NotesDatabase.COL_CONTENT,
            NotesDatabase.COL_UPDATED_AT
        )
    }
}
