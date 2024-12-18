package com.example.uf1_proyecto_sonidos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.R
import com.example.uf1_proyecto_sonidos.data.database.entities.Category

class CategoryAdapter(
    // Este es un callback que se ejecuta dentro del click del listener
    val onClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryViewHolder>() {
    private var categoryList = mutableListOf<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_row, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category, onClick)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun updateCategories(newCategories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(newCategories)
        notifyDataSetChanged()
    }
}
