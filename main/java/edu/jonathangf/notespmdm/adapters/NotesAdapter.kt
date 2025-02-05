package edu.jonathangf.notespmdm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.jonathangf.notespmdm.databinding.ItemNoteBinding
import edu.jonathangf.notespmdm.model.Note

/**
 * Class NotesAdapter.kt
 * Adaptador para la lista de notas
 * @param onNoteClick Listener para cuando se hace click en una nota
 * @param onNoteDeleteClick Listener para cuando se hace click en el icono de borrar
 * @author JonathanGF
 */
class NotesAdapter(
    private val onNoteClick: (idNote: Long) -> Unit,
    private val onNoteDeleteClick: (Note, Int) -> Unit
) : ListAdapter<Note, NotesAdapter.NotesViewHolder>(NotesDiffCallback()) {

    // ViewHolder que utiliza View Binding
    inner class NotesViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.tvTitle.text = note.title
            binding.tvDate.text = note.date

            // Configurar listeners
            binding.root.setOnClickListener {
                onNoteClick(note.idNote)
            }

            binding.ivDelete.setOnClickListener {
                onNoteDeleteClick(note, adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotesViewHolder(binding)
    }

    // Vincular los datos al ViewHolder
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote = getItem(position)
        holder.bind(currentNote)
    }

    // Callback para calcular las diferencias entre listas
    class NotesDiffCallback : DiffUtil.ItemCallback<Note>() {
        // Comprobar si los elementos son los mismos en la lista por su id
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.idNote == newItem.idNote
        }
        // Comprobar si los contenidos de los elementos son los mismos
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}