package com.example.uf1_proyecto_sonidos.ui.adapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.R

class CategoryViewHolder(categoryRowView: View) : RecyclerView.ViewHolder(categoryRowView) {
    val categoryRow: TextView = categoryRowView.findViewById(R.id.category_row)
}