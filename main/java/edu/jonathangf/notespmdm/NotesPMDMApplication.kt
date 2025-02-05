package edu.jonathangf.notespmdm

import android.app.Application
import androidx.room.Room
import edu.jonathangf.notespmdm.data.NotesDatabase

/**
 * Class NotesPMDMApplication.kt
 * Clase que extiende de Application y se encarga de inicializar la base de datos de Room
 * @author JonathanGF
 */
class NotesPMDMApplication : Application() {
    lateinit var notesDatabase: NotesDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        notesDatabase = Room.databaseBuilder(this,
            NotesDatabase::class.java, "notes-db").build()
    }
}