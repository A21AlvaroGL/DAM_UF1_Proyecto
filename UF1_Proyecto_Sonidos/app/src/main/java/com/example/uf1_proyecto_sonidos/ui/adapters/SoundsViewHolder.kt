package com.example.uf1_proyecto_sonidos.ui.adapters

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.R
import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundsViewHolder(soundButtonView: View, soundFragmentView: View) : RecyclerView.ViewHolder(soundButtonView) {
    val soundButton: Button = soundButtonView.findViewById(R.id.sound_button)
    val soundButtonText: TextView = soundButtonView.findViewById(R.id.sound_button_text)

    fun bind(sound: Sound, speed: Float, volume: Float) {
        soundButtonText.text = sound.name
        soundButtonText.isSelected = true

        soundButton.setOnClickListener {
            val uri = Uri.parse(sound.path)
            playSound(uri, speed, volume)
        }

        soundButton.setOnLongClickListener {
            val context = itemView.context
            Toast.makeText(context, "ID: ${sound.id}", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun playSound(uri: Uri, speed: Float, volume: Float) {
        val context = itemView.context

        ensureUriPermissions(context, uri)

        val mediaPlayer = MediaPlayer()
        try {
            // El ContentResolver se usa para acceder al archivo a través de la URI
            val resolver = context.contentResolver
            val assetFileDescriptor = resolver.openAssetFileDescriptor(uri, "r")

            if (assetFileDescriptor != null) {
                mediaPlayer.setDataSource(assetFileDescriptor.fileDescriptor)
                mediaPlayer.prepare()
                mediaPlayer.start()

                // Hacer que el botón tenga un estilo diferente miestras el sonido se reproduce
                // para mejorar el rendimiento uso una corrutina que evita que se ejecute en el hilo principal
                CoroutineScope(Dispatchers.Main).launch {
                    while (mediaPlayer.isPlaying) {
                        soundButton.setBackgroundResource(R.drawable.custom_sound_button_pressed)
                        delay(100)
                    }
                    soundButton.setBackgroundResource(R.drawable.custom_sound_button_not_pressed)
                }
                // Configurar la velocidad y el volumen del sonido
                mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(speed)
                mediaPlayer.setVolume(volume, volume)
            } else {
                Log.e("SoundsViewHolder", "Failed to open AssetFileDescriptor for URI: $uri")
            }
        } catch (e: Exception) {
            Log.e("SoundsViewHolder", "Error while playing sound from URI: $uri", e)
        }
    }

    // Este método comprueba si se tienen los permisos necesarios para acceder al sonido
    private fun ensureUriPermissions(context: Context, uri: Uri) {
        val resolver = context.contentResolver
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        try {
            resolver.takePersistableUriPermission(uri, flags)
        } catch (e: Exception) {
            Log.e("SoundsViewHolder", "Unable to take URI permission: $uri", e)
        }
    }
}