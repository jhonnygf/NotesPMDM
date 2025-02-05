package edu.jonathangf.notespmdm.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.jonathangf.notespmdm.data.NotesRepository
import edu.jonathangf.notespmdm.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * Class MainViewModel.kt
 * Clase que representa el ViewModel de la aplicación principal de notas
 * @param repository Repositorio de la aplicación
 * @author JonathanGF
 */
class MainViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _isSorted = MutableStateFlow(false)

    private val _currentNotes: Flow<List<Note>> =
        combine(repository.allNotes, _isSorted) { notes, isSorted ->
            if (isSorted) {
                notes.sortedBy { it.title?.lowercase() }
            } else {
                notes.sortedByDescending { it.date }
            }
        }
    val currentSortedNotes: Flow<List<Note>> = _currentNotes

    /**
     * Clase MainViewModel.kt
     * Realiza el insert de una nota en la base de datos
     * @author JonathanGF
     */
    fun saveNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    /**
     * Clase MainViewModel.kt
     * Realiza el delete de una nota en la base de datos
     * @param note Nota a eliminar
     * @author JonathanGF
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    /**
     * Clase MainViewModel.kt
     * Ordena las notas por título
     * @author JonathanGF
     */
    fun sortNotesByTitle() {
        _isSorted.value = true
    }

    /**
     * Clase MainViewModel.kt
     * Desactiva el filtro y muestra todas las notas sin ordenar
     * @author JonathanGF
     */
    fun deactivateFilter() {
        _isSorted.value = false
    }
}

/**
 * Clase MainViewModelFactory.kt
 * Clase que representa el Factory del ViewModel de la aplicación principal de notas
 * @param notesRepository Repositorio de la aplicación
 * @author JonathanGF
 */
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val notesRepository: NotesRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(notesRepository) as T
    }
}