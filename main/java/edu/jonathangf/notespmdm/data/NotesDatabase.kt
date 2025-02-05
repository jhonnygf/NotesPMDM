package edu.jonathangf.notespmdm.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import edu.jonathangf.notespmdm.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Class NotesDatabase.kt
 * Clase que representa la base de datos de la aplicación
 * @constructor Crea una instancia de la base de datos
 * @author JonathanGF
 */
@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    //Aqui se definen las operaciones que se pueden hacer con la base de datos
    @Dao
    interface NotesDao {
        //Aqui se le pasa la nota para que la inserte en la base de datos
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertNote(note: Note): Long

        //Aqui se le pasa la nota para que la actualice en la base de datos
        @Update
        suspend fun updateNote(note: Note): Int

        //Aqui se le pasa la nota para que la elimine de la base de datos
        @Delete
        suspend fun deleteNote(note: Note): Int

        //Aqui se le pasa el id de la nota para que devuelva la nota
        @Query("SELECT * FROM notes WHERE idNote = :id LIMIT 1")
        suspend fun getNoteById(id: Long): Note?

        //Aqui se le pasa el id de la nota para que devuelva la nota
        @Query("SELECT * FROM notes ORDER BY title")
        fun getAllNotes(): Flow<List<Note>>

        //Aqui se le pasa el id de la nota para que devuelva la nota
        @Query("SELECT * FROM notes WHERE idNote = :noteId")
        suspend fun getNoteId(noteId: Long): Note?
    }
}