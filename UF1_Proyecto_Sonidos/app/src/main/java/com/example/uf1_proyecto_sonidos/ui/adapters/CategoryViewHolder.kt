package com.example.uf1_proyecto_sonidos.ui.adapters

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.R
import com.example.uf1_proyecto_sonidos.data.database.entities.Category

class CategoryViewHolder(categoryRowView: View) : RecyclerView.ViewHolder(categoryRowView) {
    val categoryRow: TextView = categoryRowView.findViewById(R.id.category_row)

    fun bind(category: Category, onClick: (Category) -> Unit) {
        categoryRow.text = category.name
        // Al pulsar en una categoría, la envío mediente la función callback onClick() para así manejar el filtrado en otro fragmento
        categoryRow.setOnClickListener {
            onClick(category)
        }
    }
}