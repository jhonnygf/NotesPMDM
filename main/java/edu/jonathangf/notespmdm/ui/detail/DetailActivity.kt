package edu.jonathangf.notespmdm.ui.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import edu.jonathangf.notespmdm.databinding.ActivityDetailBinding
import edu.jonathangf.notespmdm.model.Note
import kotlinx.coroutines.launch
import edu.jonathangf.notespmdm.NotesPMDMApplication
import edu.jonathangf.notespmdm.R
import edu.jonathangf.notespmdm.data.NotesRepository
import edu.jonathangf.notespmdm.data.NotesDataSource


/**
 * Clase DetailActivity.kt
 * Clase que representa la actividad de detalle de la aplicación, en ella se pueden visualizar los
 * detalles de una nota existente o añadir una nueva.
 * @author JonathanGF
 */
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var noteIdAux: Long = 0L

    private val viewModel: DetailViewModel by viewModels {
        val db = (application as NotesPMDMApplication).notesDatabase
        val dataSource = NotesDataSource(db.notesDao())
        val repository = NotesRepository(dataSource)
        DetailViewModelFactory(repository, noteIdAux)
    }

    companion object {
        private const val NOTE_ID = "note_id"

        fun navigate(activity: Activity, noteId: Long = 0L) {
            val intent = Intent(activity, DetailActivity::class.java).apply {
                putExtra(NOTE_ID, noteId)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        noteIdAux = intent.getLongExtra(NOTE_ID, 0L)

        setSupportActionBar(binding.mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (noteIdAux == 0L) {
            supportActionBar?.title = getString(R.string.txt_opAddNote)
        } else {
            supportActionBar?.title = getString(R.string.txt_opEditNote)
            loadNoteDetails(noteIdAux)
        }

        binding.mToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.opd_save -> {
                    saveNote()
                    true
                }

                else -> false
            }
        }
    }

    /**
     * Class DetailActivity.kt
     * Carga los detalles de la nota para editar
     * @param noteId Identificador de la nota
     * @author JonathanGF
     */
    private fun loadNoteDetails(noteId: Long) {
        lifecycleScope.launch {
            val note = viewModel.getNoteById(noteId)
            note?.let {
                binding.etTitle.setText(it.title)
                binding.etDescription.setText(it.description)
            }
        }
    }

    /**
     * Class DetailActivity.kt
     * Guarda la nota añadir o editar en la base de datos si el título esta vacío muestra un error
     * en el campo de título
     * @author JonathanGF
     */
    private fun saveNote() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val date = java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            java.util.Locale.getDefault()
        ).format(java.util.Date())

        if (title.isEmpty()) {
            binding.tilTitle.error = getString(R.string.txt_errorTitle)
            return
        }

        val note = Note(
            idNote = if (noteIdAux == 0L) 0L else noteIdAux,
            title = title,
            description = if (description.isNotEmpty()) description else null,
            date = date
        )

        viewModel.saveNote(note)
        finish()
    }

    /**
     * Class DetailActivity.kt
     * Crea el menú de la Toolbar con la opción de guardar la nota
     * @author JonathanGF
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    /**
     * Class DetailActivity.kt
     * Listener de la opción de guardar la nota en la Toolbar, llama a la función saveNote() para
     * guardar la nota
     * @param item Elemento del menú seleccionado
     * @author JonathanGF
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.opd_save -> {
                saveNote()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Class DetailActivity.kt
     * Listener de la flecha de la Toolbar para volver a la actividad anterior
     * @author JonathanGF
     */
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}