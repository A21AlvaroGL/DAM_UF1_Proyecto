package com.example.uf1_proyecto_sonidos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SoundsAdapter : RecyclerView.Adapter<SoundsViewHolder>() {
    private val soundsList: List<String> = generateSoundsList(50)

    // Este método solo sirve para generar una lista de prueba con 50 elementos mientras no creo la base de datos
    private fun generateSoundsList(num: Int): List<String> {
        val list = mutableListOf<String>()
        for (i in 1..num) {
            list.add("Sound $i")
        }
        return list
    }

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
        val soundName = soundsList[position]
        holder.bind(soundName)
    }
}