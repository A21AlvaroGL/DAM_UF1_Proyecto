package com.example.uf1_proyecto_sonidos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.uf1_proyecto_sonidos.data.database.AppDatabase
import com.example.uf1_proyecto_sonidos.data.database.entities.Category
import com.example.uf1_proyecto_sonidos.data.events.CategoryEvent
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
        Room.databaseBuilder(
            requireContext().applicationContext,
            AppDatabase::class.java,
            "avisonus.db"
        ).build()
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

    // Variables que almacenan los datos de los formulario
    private lateinit var soundNameEditText: EditText
    private lateinit var soundCategorySpinner: Spinner
    private lateinit var soundPathButton: Button
    private lateinit var addSoundButton: Button

    private lateinit var categoryNameEditText: EditText
    private lateinit var addCategoryButton: Button

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

        // Formualrio de subir sonido
        soundNameEditText = view.findViewById(R.id.sound_name_text)
        soundCategorySpinner = view.findViewById(R.id.sound_category_spinner)
        soundPathButton = view.findViewById(R.id.sound_path_button)
        addSoundButton = view.findViewById(R.id.add_sound_button)

        addSoundButton.setOnClickListener {
            val soundName = soundNameEditText.text.toString()
            val soundCategory = soundCategorySpinner.selectedItem
        }

        // Formulario de añadir acategoría
        categoryNameEditText = view.findViewById(R.id.category_name_text)
        addCategoryButton = view.findViewById(R.id.add_category_button)

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

        return view;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UploadFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UploadFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}