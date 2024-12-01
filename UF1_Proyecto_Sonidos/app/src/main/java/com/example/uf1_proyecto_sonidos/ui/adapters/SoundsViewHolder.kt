package com.example.uf1_proyecto_sonidos.ui.adapters

import android.media.MediaPlayer
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
            playSound("res/raw/punch.mp3")
        }
    }

    private fun playSound(path: String) {
        val context = itemView.context

        val mediaPlayer = MediaPlayer.create(context, R.raw.punch)
        val speed = 1f
        mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(speed)
        mediaPlayer.start()

    }
}