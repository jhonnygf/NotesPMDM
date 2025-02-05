package edu.jonathangf.notespmdm.data

import edu.jonathangf.notespmdm.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Class NotesDataSource.kt
 * Clase que representa el origen de datos de la aplicación
 * @param db Base de datos de la aplicación
 * @author JonathanGF
 */
class NotesDataSource(private val db : NotesDatabase.NotesDao) {

    // Aquí se definen las operaciones que se pueden hacer con la base de datos
    val allNotes: Flow<List<Note>> = db.getAllNotes()

    // Aquí se le pasa la nota para que la inserte en la base de datos
    suspend fun insertNote(note: Note) { db.insertNote(note) }

    // Aquí se le pasa la nota para que la actualice en la base de datos
    suspend fun updateNote(note: Note): Int = db.updateNote(note)

    // Aquí se le pasa la nota para que la elimine de la base de datos
    suspend fun deleteNote(note: Note) { db.deleteNote(note) }

    // Aquí se le pasa el id de la nota para que devuelva la nota
    suspend fun getNoteById(noteId: Long): Note? = db.getNoteId(noteId)
}