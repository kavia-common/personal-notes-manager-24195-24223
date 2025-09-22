package org.example.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R
import org.example.app.models.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * PUBLIC_INTERFACE
 * NotesAdapter binds Note items to the RecyclerView list.
 */
class NotesAdapter(
    private val items: MutableList<Note>,
    private val listener: NoteClickListener
) : RecyclerView.Adapter<NotesAdapter.VH>() {

    interface NoteClickListener {
        // PUBLIC_INTERFACE
        fun onNoteClicked(note: Note)
    }

    fun update(newItems: List<Note>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val note = items[position]
        holder.bind(note)
        holder.itemView.setOnClickListener {
            it.animate().alpha(0.6f).setDuration(80).withEndAction {
                it.alpha = 1f
                listener.onNoteClicked(note)
            }.start()
        }
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        private val tvUpdated: TextView = itemView.findViewById(R.id.tvUpdated)

        fun bind(note: Note) {
            tvTitle.text = if (note.title.isBlank()) "(Untitled)" else note.title
            tvContent.text = note.content
            tvUpdated.text = "${itemView.context.getString(R.string.updated_prefix)} ${formatDate(note.updatedAt)}"
        }

        private fun formatDate(ms: Long): String {
            val df = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault())
            return df.format(Date(ms))
        }
    }
}
