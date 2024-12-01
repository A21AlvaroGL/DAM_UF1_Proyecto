package com.example.uf1_proyecto_sonidos.ui.adapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.R

class SoundsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val soundButton: Button = itemView.findViewById(R.id.sound_button)
    val soundButtonText: TextView = itemView.findViewById(R.id.sound_button_text)

    fun bind(soundName: String) {


        soundButtonText.text = soundName
        soundButtonText.isSelected = true
        soundButton.setOnClickListener {

        }
    }
}