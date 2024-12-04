package com.example.uf1_proyecto_sonidos.ui.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.R
import com.example.uf1_proyecto_sonidos.data.database.entities.Category
import com.example.uf1_proyecto_sonidos.data.database.entities.Sound

class CategoryViewHolder(categoryRowView: View) : RecyclerView.ViewHolder(categoryRowView) {
    val categoryRow: TextView = categoryRowView.findViewById(R.id.category_row)
    private var categoryId: Int = 0;

    fun bind(category: Category) {
        categoryRow.text = category.name
        categoryRow.setOnClickListener {
            categoryId = category.id
        }
    }

    fun getCategoryId(): Int {
        return categoryId
    }
}