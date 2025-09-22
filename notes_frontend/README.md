# Ocean Notes - Kotlin Android App

A modern notes app using traditional Android Views (no Compose), SQLite for local persistence, and an "Ocean Professional" theme.

Features:
- Create, edit, search, and delete notes
- RecyclerView list with search bar and FAB
- Smooth transitions and accent highlights
- Local SQLite storage via SQLiteOpenHelper

Build:
- ./gradlew build
- :app:installDebug

Packages:
- org.example.app.ui.MainActivity (launcher)
- org.example.app.ui.EditNoteActivity
- org.example.app.data.NotesRepository / NotesDatabase
- org.example.app.models.Note
