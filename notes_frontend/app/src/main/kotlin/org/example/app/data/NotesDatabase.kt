package org.example.app.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * PUBLIC_INTERFACE
 * NotesDatabase is the SQLite helper that manages schema creation and upgrades.
 *
 * Use this class indirectly via NotesRepository.
 */
class NotesDatabase(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_NOTES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT NOT NULL,
                $COL_CONTENT TEXT NOT NULL,
                $COL_UPDATED_AT INTEGER NOT NULL
            );
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX idx_notes_updated ON $TABLE_NOTES($COL_UPDATED_AT);")
        db.execSQL("CREATE INDEX idx_notes_title ON $TABLE_NOTES($COL_TITLE);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Simple migration strategy for this sample app
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    companion object {
        const val DB_NAME = "notes.db"
        const val DB_VERSION = 1

        const val TABLE_NOTES = "notes"
        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_CONTENT = "content"
        const val COL_UPDATED_AT = "updated_at"
    }
}
