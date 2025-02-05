package edu.jonathangf.notespmdm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Class Note.kt
 * Clase que representa una nota de la aplicación
 * @param idNote Identificador de la nota
 * @param title Título de la nota
 * @param description Descripción de la nota
 * @param date Fecha de la nota
 * @constructor Crea una instancia de la nota
 * @author JonathanGF
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val idNote: Long = 0,
    val title: String? = null,
    val description: String? = null,
    val date: String? = null
)
