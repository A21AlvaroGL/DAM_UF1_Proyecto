package com.example.uf1_proyecto_sonidos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.data.database.AppDatabase
import com.example.uf1_proyecto_sonidos.data.events.SoundEvent
import com.example.uf1_proyecto_sonidos.data.sort_types.SoundSortType
import com.example.uf1_proyecto_sonidos.data.view_models.CategoryViewModel
import com.example.uf1_proyecto_sonidos.data.view_models.SoundViewModel
import com.example.uf1_proyecto_sonidos.ui.adapters.CategoryAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CategoryBottomSheetFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var recyclerView: RecyclerView

    // Instancio la base de datos y los view model
    private val db by lazy {
        AppDatabase.getDatabase(requireContext().applicationContext)
    }


    private val categoryViewModel by viewModels<CategoryViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CategoryViewModel(db.categoryDAO()) as T
                }
            }
        }
    )

    private val soundViewModel by viewModels<SoundViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SoundViewModel(db.soundDAO()) as T
                }
            }
        }
    )

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
        val view = inflater.inflate(R.layout.fragment_category_bottom_sheet, container, false)
        recyclerView = view.findViewById(R.id.categories_recycler)

        // Configuro el adaptador para manejar la selección de categorías
        categoryAdapter = CategoryAdapter { selectedCategory ->
            soundViewModel.onEvent(SoundEvent.SortSounds(SoundSortType.CATEGORY))
            soundViewModel.onEvent(SoundEvent.FilterByCategory(selectedCategory.id))
            // Cierro el fragmento una vez seleccionada la categoría
            dismiss()
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = categoryAdapter

        // Observo los cambios en el estado de las categorías y actualizo el adaptador
        lifecycleScope.launchWhenStarted {
            categoryViewModel.state.collect { state ->
                categoryAdapter.updateCategories(state.categories)
            }
        }
        return view
    }
}