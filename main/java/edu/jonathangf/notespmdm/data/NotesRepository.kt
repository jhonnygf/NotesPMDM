package edu.jonathangf.notespmdm.data

import edu.jonathangf.notespmdm.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Class NotesRepository.kt
 * Clase que representa el repositorio de la aplicación
 * @param dataSource Origen de datos de la aplicación
 * @constructor Create empty NotesRepository
 */
class NotesRepository (private val dataSource: NotesDataSource) {

    // Aquí se definen las operaciones que se pueden hacer con la base de datos
    val allNotes : Flow<List<Note>> = dataSource.allNotes

    // Aquí se le pasa la nota para que la inserte en la base de datos
    suspend fun insertNote(note : Note) { dataSource.insertNote(note) }

    // Aquí se le pasa la nota para que la actualice en la base de datos
    suspend fun updateNote(note: Note): Int = dataSource.updateNote(note)

    // Aquí se le pasa la nota para que la elimine de la base de datos
    suspend fun deleteNote(note : Note) { dataSource.deleteNote(note) }

    // Aquí se le pasa el id de la nota para que devuelva la nota
    suspend fun getNoteById(id: Long): Note? = dataSource.getNoteById(id)
}