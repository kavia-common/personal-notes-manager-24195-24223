package org.example.app.ui

import org.example.app.R
import org.example.app.data.NotesRepository
import org.example.app.models.Note
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * PUBLIC_INTERFACE
 * MainActivity (UI package) as the app launcher activity.
 */
class MainActivity : Activity(), NotesAdapter.NoteClickListener {

    private lateinit var repository: NotesRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesAdapter
    private lateinit var emptyState: TextView
    private lateinit var searchInput: EditText
    private lateinit var clearSearch: ImageButton
    private lateinit var fab: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = NotesRepository(this)

        recyclerView = findViewById(R.id.notesRecycler)
        emptyState = findViewById(R.id.emptyState)
        searchInput = findViewById(R.id.searchInput)
        clearSearch = findViewById(R.id.clearSearch)
        fab = findViewById(R.id.fabAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotesAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            it.animate().rotationBy(360f).setDuration(300).setInterpolator(AccelerateDecelerateInterpolator()).start()
            startActivity(Intent(this, EditNoteActivity::class.java))
        }

        clearSearch.setOnClickListener { searchInput.setText("") }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loadNotes(s?.toString()?.trim() ?: "")
                clearSearch.visibility = if (s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        loadNotes(searchInput.text?.toString()?.trim() ?: "")
    }

    private fun loadNotes(query: String) {
        val notes: List<Note> = if (query.isEmpty()) repository.getAllNotes() else repository.searchNotes(query)
        adapter.update(notes)
        emptyState.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        recyclerView.visibility = if (notes.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onNoteClicked(note: Note) {
        val intent = Intent(this, EditNoteActivity::class.java)
        intent.putExtra(EditNoteActivity.EXTRA_NOTE_ID, note.id)
        startActivity(intent)
    }
}
