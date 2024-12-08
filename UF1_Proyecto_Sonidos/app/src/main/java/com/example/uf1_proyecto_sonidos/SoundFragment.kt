package com.example.uf1_proyecto_sonidos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.data.database.AppDatabase
import com.example.uf1_proyecto_sonidos.data.events.SoundEvent
import com.example.uf1_proyecto_sonidos.data.view_models.CategoryViewModel
import com.example.uf1_proyecto_sonidos.data.view_models.SoundViewModel
import com.example.uf1_proyecto_sonidos.ui.adapters.SoundsAdapter
import com.google.android.material.slider.Slider

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SoundFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // Instancio la base de datos y el view model
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

    private lateinit var soundsAdapter: SoundsAdapter
    private lateinit var soundsRecyclerView: RecyclerView
    private lateinit var speedSlider: Slider
    private lateinit var volumeSlider: Slider
    private lateinit var filterByCategory: ImageButton


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
        val view = inflater.inflate(R.layout.fragment_sound, container, false)
        soundsRecyclerView = view.findViewById<RecyclerView>(R.id.sounds_recycler)
        soundsAdapter = SoundsAdapter()
        soundsRecyclerView.adapter = soundsAdapter
        filterByCategory = view.findViewById(R.id.filter_by_category_button)

        // Actualiza la lista de sonidos en el RecyclerView cuando el estado cambia
        lifecycleScope.launchWhenStarted {
            soundViewModel.state.collect { state ->
                soundsAdapter.updateSounds(state.sounds)
            }
        }

        // Aquí paso la velocidad y el volumen, cada vez que se modifican, del slider al adaptador de la recycler view
        speedSlider = view.findViewById(R.id.speed_bar)
        speedSlider.addOnChangeListener { slider, value, fromUser ->
            soundsAdapter.updateSpeed(value)
        }

        volumeSlider = view.findViewById(R.id.volume_bar)
        volumeSlider.addOnChangeListener { slider, value, fromUser ->
            soundsAdapter.updateVolume(value)
        }

        // Despliega el bottomSheetFragment
        filterByCategory.setOnClickListener {
            val bottomSheet = CategoryBottomSheetFragment()
            bottomSheet.show(parentFragmentManager, "CategoriesBottomSheet")
        }

        return view
    }
}