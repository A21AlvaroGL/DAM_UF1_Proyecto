package com.example.uf1_proyecto_sonidos.ui.adapters

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.R
import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import java.io.FileDescriptor

class SoundsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val soundButton: Button = itemView.findViewById(R.id.sound_button)
    val soundButtonText: TextView = itemView.findViewById(R.id.sound_button_text)

    fun bind(sound: Sound) {
        soundButtonText.text = sound.name
        soundButtonText.isSelected = true

        soundButton.setOnClickListener {
            val uri = Uri.parse(sound.path)
            playSound(uri)
        }
    }

    private fun playSound(uri: Uri) {
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

                // Configurar la velocidad del sonido (opcional)
                mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(1f)
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