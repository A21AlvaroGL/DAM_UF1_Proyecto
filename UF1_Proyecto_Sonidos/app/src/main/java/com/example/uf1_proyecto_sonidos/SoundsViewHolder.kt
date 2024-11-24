package com.example.uf1_proyecto_sonidos

import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class SoundsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val soundButton: Button = itemView.findViewById(R.id.sound_button)

    fun bind(soundName: String) {
        soundButton.text = soundName

        soundButton.setOnClickListener {

        }
    }
}