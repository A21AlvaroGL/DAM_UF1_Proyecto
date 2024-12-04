package com.example.uf1_proyecto_sonidos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SoundFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoundFragment : Fragment() {
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

    private lateinit var soundsAdapter: SoundsAdapter
    private lateinit var soundsRecyclerView: RecyclerView
    private lateinit var speedSlider: Slider
    private lateinit var volumeSlider: Slider
    private lateinit var filterByCategory: Button


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

        filterByCategory.setOnClickListener {
            val bottomSheet = CategoryBottomSheetFragment()
            bottomSheet.show(parentFragmentManager, "CategoriesBottomSheet")
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SoundFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SoundFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}