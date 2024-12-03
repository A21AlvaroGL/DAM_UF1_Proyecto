package com.example.uf1_proyecto_sonidos

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.uf1_proyecto_sonidos.data.database.AppDatabase
import com.example.uf1_proyecto_sonidos.data.database.entities.Category
import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import com.example.uf1_proyecto_sonidos.data.events.CategoryEvent
import com.example.uf1_proyecto_sonidos.data.events.SoundEvent
import com.example.uf1_proyecto_sonidos.data.view_models.CategoryViewModel
import com.example.uf1_proyecto_sonidos.data.view_models.SoundViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UploadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val db by lazy {
        AppDatabase.getDatabase(requireContext().applicationContext)
    }

    private val soundViewModel by viewModels<SoundViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SoundViewModel(db.soundDAO()) as T
                }
            }
        }
    )

    private val categoryViewModel by viewModels<CategoryViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CategoryViewModel(db.categoryDAO()) as T
                }
            }
        }
    )
    // Variables para cambiar entre el tipo de formularios
    private lateinit var formRadioGroup: RadioGroup
    private lateinit var uploadForms: LinearLayout
    private lateinit var deleteForms: LinearLayout

    // Variables que almacenan los datos de los formulario
    private lateinit var soundNameEditText: EditText
    private lateinit var soundCategorySpinner: Spinner
    private lateinit var deleteCategorySpinner: Spinner
    private lateinit var soundPathButton: Button
    private lateinit var addSoundButton: Button
    private lateinit var  soundPath: String
    companion object {
        private const val PICK_SOUND_FILE = 2
    }

    private lateinit var categoryNameEditText: EditText
    private lateinit var addCategoryButton: Button
    private lateinit var deleteCategoryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upload, container, false)

        // Inicializar las vistas
        formRadioGroup = view.findViewById(R.id.upload_delete_radio_group)
        uploadForms = view.findViewById(R.id.upload_forms)
        deleteForms = view.findViewById(R.id.delete_forms)
        soundNameEditText = view.findViewById(R.id.sound_name_text)
        soundCategorySpinner = view.findViewById(R.id.sound_category_spinner)
        deleteCategorySpinner = view.findViewById(R.id.delete_category_spinner)
        soundPathButton = view.findViewById(R.id.sound_path_button)
        addSoundButton = view.findViewById(R.id.add_sound_button)
        categoryNameEditText = view.findViewById(R.id.category_name_text)
        addCategoryButton = view.findViewById(R.id.add_category_button)
        deleteCategoryButton = view.findViewById(R.id.delete_category_button)

        showForms()
        configureSpinner()
        addSound()
        addCategory()
        deleteCategory()

        return view
    }

    private fun configureSpinner() {
        // Configurar el spinner con las categorías
        lifecycleScope.launchWhenStarted {
            categoryViewModel.state.collect { state ->
                val categories = state.categories
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    categories.map { it.name }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                soundCategorySpinner.adapter = adapter
                deleteCategorySpinner.adapter = adapter
            }
        }
    }

    private fun addSound() {
        soundPathButton.setOnClickListener {
            openFile()
        }

        addSoundButton.setOnClickListener {
            val soundName = soundNameEditText.text.toString()
            val selectedCategoryPosition = soundCategorySpinner.selectedItemPosition
            val categories = categoryViewModel.state.value.categories

            if (soundName.isNotBlank() && selectedCategoryPosition >= 0 && ::soundPath.isInitialized && soundPath.isNotBlank()) {
                if (categories.isNotEmpty()) {
                    val selectedCategory = categories[selectedCategoryPosition]
                    val soundCategoryId = selectedCategory.id

                    soundViewModel.onEvent(
                        SoundEvent.SaveSound(
                            Sound(
                                name = soundName,
                                path = soundPath,
                                category_id = soundCategoryId
                            )
                        )
                    )
                }
                Toast.makeText(activity, getString(R.string.data_uploaded_toast), Toast.LENGTH_SHORT).show()
                soundNameEditText.text.clear()
            } else {
                Toast.makeText(activity, getString(R.string.empty_data_toast), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showForms() {
        // Configurar el radio group para que muestre uno formularios un otros
        formRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_upload -> {
                    deleteForms.visibility = View.GONE
                    uploadForms.visibility = View.VISIBLE
                }
                R.id.radio_delete -> {
                    uploadForms.visibility = View.GONE
                    deleteForms.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun addCategory() {
        addCategoryButton.setOnClickListener {
            val categoryName = categoryNameEditText.text.toString()

            if (categoryName.isNotBlank()) {
                categoryViewModel.onEvent(
                    CategoryEvent.SaveCategory(
                        Category(name = categoryName)
                    )
                )
                Toast.makeText(activity, getString(R.string.data_uploaded_toast), Toast.LENGTH_SHORT).show()
                categoryNameEditText.text.clear()
            } else {
                Toast.makeText(activity, getString(R.string.empty_data_toast), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteSound() {

    }

    private fun deleteCategory() {
        deleteCategoryButton.setOnClickListener {
            val categories = categoryViewModel.state.value.categories
            val selectedCategoryPosition = deleteCategorySpinner.selectedItemPosition

            if (categories.isNotEmpty() && selectedCategoryPosition >= 0) {
                val selectedCategory = categories[selectedCategoryPosition]

                categoryViewModel.onEvent(
                    CategoryEvent.DeleteCategory(
                        selectedCategory
                    )
                )
                Toast.makeText(activity, getString(R.string.data_deleted_toast), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, getString(R.string.empty_data_toast), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "audio/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("audio/mpeg"))
        }
        startActivityForResult(intent, PICK_SOUND_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_SOUND_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                soundPath = uri.toString()

                val resolver = requireContext().contentResolver
                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                try {
                    resolver.takePersistableUriPermission(uri, flags)
                } catch (e: Exception) {
                    Log.e("UploadFragment", "Unable to take URI permission: $uri", e)
                }
            }
        }
    }
}