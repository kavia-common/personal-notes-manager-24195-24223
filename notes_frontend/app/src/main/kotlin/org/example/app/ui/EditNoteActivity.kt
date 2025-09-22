package org.example.app.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import org.example.app.R
import org.example.app.data.NotesRepository
import org.example.app.models.Note

/**
 * PUBLIC_INTERFACE
 * EditNoteActivity allows creating a new note or editing an existing one.
 *
 * Input via Intent extra EXTRA_NOTE_ID (optional): if present, loads that note for editing.
 * Returns: none (finishes and returns to MainActivity).
 */
class EditNoteActivity : Activity() {

    private lateinit var repository: NotesRepository
    private var noteId: Long = 0L

    private lateinit var inputTitle: EditText
    private lateinit var inputContent: EditText
    private lateinit var btnSave: TextView
    private lateinit var btnDelete: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        setContentView(R.layout.activity_edit_note)

        repository = NotesRepository(this)

        inputTitle = findViewById(R.id.inputTitle)
        inputContent = findViewById(R.id.inputContent)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)

        noteId = intent.getLongExtra(EXTRA_NOTE_ID, 0L)
        if (noteId != 0L) {
            val note = repository.getNote(noteId)
            if (note != null) populate(note)
        }

        btnDelete.visibility = if (noteId != 0L) View.VISIBLE else View.GONE

        btnSave.setOnClickListener {
            saveNote()
        }

        btnDelete.setOnClickListener {
            if (noteId != 0L) {
                repository.deleteNote(noteId)
                finishWithTransition()
            }
        }
    }

    private fun populate(note: Note) {
        inputTitle.setText(note.title)
        inputContent.setText(note.content)
    }

    private fun saveNote() {
        val title = inputTitle.text?.toString()?.trim() ?: ""
        val content = inputContent.text?.toString()?.trim() ?: ""
        if (noteId == 0L) {
            repository.insertNote(title, content)
        } else {
            repository.updateNote(noteId, title, content)
        }
        finishWithTransition()
    }

    private fun finishWithTransition() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }
}
