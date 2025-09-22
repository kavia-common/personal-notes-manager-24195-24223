package org.example.app.models

/**
 * PUBLIC_INTERFACE
 * Data class representing a note entity.
 *
 * @property id Unique identifier in the local database (0 for new notes).
 * @property title Title of the note.
 * @property content Body/content of the note.
 * @property updatedAt Last updated timestamp in milliseconds.
 */
data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val updatedAt: Long
)
