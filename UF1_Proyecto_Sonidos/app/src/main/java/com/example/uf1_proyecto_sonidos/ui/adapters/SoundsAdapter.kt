package com.example.uf1_proyecto_sonidos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.R
import com.example.uf1_proyecto_sonidos.data.database.entities.Sound

class SoundsAdapter() : RecyclerView.Adapter<SoundsViewHolder>() {
    private var soundsList = mutableListOf<Sound>()
    private var speed: Float = 1.0f

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundsViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val soundButtonView = inflater.inflate(R.layout.sound_button, parent, false)
        val soundFragmentView = inflater.inflate(R.layout.fragment_sound, parent, false)
        return SoundsViewHolder(soundButtonView, soundFragmentView)
    }

    override fun getItemCount(): Int {
        return soundsList.size
    }

    override fun onBindViewHolder(holder: SoundsViewHolder, position: Int) {
        val sound = soundsList[position]
        holder.bind(sound, speed)
    }

    fun updateSounds(newSounds: List<Sound>) {
        soundsList.clear()
        soundsList.addAll(newSounds)
        notifyDataSetChanged()
    }

    fun updateSpeed(speed: Float) {
        this.speed = speed
        notifyDataSetChanged()
    }
}