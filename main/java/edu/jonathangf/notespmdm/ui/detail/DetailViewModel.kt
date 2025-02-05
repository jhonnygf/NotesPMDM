package edu.jonathangf.notespmdm.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.jonathangf.notespmdm.data.NotesRepository
import edu.jonathangf.notespmdm.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de detalle de una nota
 * @param repository Repositorio de notas
 * @param idNote Identificador de la nota
 * @author JonathanGF
 */
class DetailViewModel (private val repository: NotesRepository, val idNote: Long) : ViewModel() {

    private val _stateNotes = MutableStateFlow(Note())
    val stateNotes: StateFlow<Note> = _stateNotes.asStateFlow()

    /**
     * Class DetailViewModel.kt
     * Inicializa el ViewModel
     * @author JonathanGF
     */
    fun saveNote(note: Note) {
        viewModelScope.launch {
            if (note.idNote == 0L) {
                repository.insertNote(note)
            } else {
                repository.updateNote(note)
            }
        }
    }

    /**
     * Clase DetailViewModel.kt
     * Obtiene la nota por su id
     * @param id Identificador de la nota
     * @author JonathanGF
     */
    suspend fun getNoteById(id: Long): Note? {
        return repository.getNoteById(id)
    }
}

/**
 * Class DetailViewModelFactory.kt
 * Clase que representa el Factory del ViewModel de la pantalla de detalle de una nota
 * @param notesRepository Repositorio de notas
 * @param idNote Identificador de la nota
 * @author JonathanGF
 */
@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    val notesRepository: NotesRepository, val idNote: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(notesRepository, idNote) as T
    }
}