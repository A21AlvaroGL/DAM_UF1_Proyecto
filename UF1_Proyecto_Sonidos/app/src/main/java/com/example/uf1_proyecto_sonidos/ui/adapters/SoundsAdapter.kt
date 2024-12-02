package com.example.uf1_proyecto_sonidos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.R
import com.example.uf1_proyecto_sonidos.data.database.entities.Sound

class SoundsAdapter(private var soundList: List<Sound>) : RecyclerView.Adapter<SoundsViewHolder>() {
    private var soundsList = mutableListOf<Sound>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundsViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.sound_button, parent, false)
        return SoundsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return soundsList.size
    }

    override fun onBindViewHolder(holder: SoundsViewHolder, position: Int) {
        val sound = soundsList[position]
        holder.bind(sound)
    }

    fun updateSounds(newSounds: List<Sound>) {
        soundsList.clear()
        soundsList.addAll(newSounds)
        notifyDataSetChanged()
    }
}