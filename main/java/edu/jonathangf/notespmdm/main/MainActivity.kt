package edu.jonathangf.notespmdm.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import edu.jonathangf.notespmdm.NotesPMDMApplication
import edu.jonathangf.notespmdm.R
import edu.jonathangf.notespmdm.adapters.NotesAdapter
import edu.jonathangf.notespmdm.data.NotesDataSource
import edu.jonathangf.notespmdm.data.NotesRepository
import edu.jonathangf.notespmdm.databinding.ActivityMainBinding
import edu.jonathangf.notespmdm.ui.detail.DetailActivity
import kotlinx.coroutines.launch

/**
 * Class MainActivity.kt
 * Clase MainActivity representa la actividad principal de la aplicación, en ella se pueden
 * visualizar las notas existentes en la lista, ordenarlas por nombre en orden alfabetico o
 * borrarlas. También se puede pulsar a la opción del menu que nos llevara a la ventana del
 * detail en la que podremos crear una nueva o acceder a esta ventana pero con los datos de una
 * nota existente pulsando sobre ella para modificar sus datos.
 * @author JonathanGF
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val vm: MainViewModel by viewModels {
        val db = (application as NotesPMDMApplication).notesDatabase
        val dataSource = NotesDataSource(db.notesDao())
        val repository = NotesRepository(dataSource)
        MainViewModelFactory(repository)
    }

    private val adapter = NotesAdapter(
        onNoteClick = { idNote ->
            DetailActivity.navigate(this, idNote)
        },
        onNoteDeleteClick = { note, adapterposition ->
            vm.deleteNote(note)
            Snackbar.make(
                binding.root,
                getString(R.string.txt_noteDeleted, note.title),
                Snackbar.LENGTH_LONG
            ).setAction(R.string.txt_undo) {
                vm.saveNote(note)
            }.show()
        }
    )

    /**
     * Clase MainActivity.kt
     * Función que se ejecuta al crear la actividad, en ella se inicializan los elementos de la actividad
     * @author JonathanGF
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.mToolbar.inflateMenu(R.menu.menu)

        binding.recyclerView.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.currentSortedNotes.collect { notes ->
                    adapter.submitList(notes)
                    if (notes.isEmpty()) {
                        binding.tvWarning.text = getString(R.string.txt_noNotes)
                        binding.tvWarning.visibility = View.VISIBLE
                    } else {
                        binding.tvWarning.visibility = View.GONE
                    }
                }
            }
        }
    }

    /**
     * Clase MainActivity.kt
     * Función que se ejecuta al iniciar la actividad, en ella se configuran los listeners de los
     * elementos de la actividad, como el toolbar y sus opciones, y se configura el listener de la
     * lista de notas para poder acceder a la ventana de detail con los datos de la nota seleccionada
     * @author JonathanGF
     */
    override fun onStart() {
        super.onStart()

        binding.mToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.opt_filter -> {
                    vm.deactivateFilter()
                    true
                }

                R.id.opt_sort -> {
                    vm.sortNotesByTitle()
                    true
                }

                R.id.opt_add -> {
                    DetailActivity.navigate(this)
                    true
                }
                else -> false
            }
        }
    }
}