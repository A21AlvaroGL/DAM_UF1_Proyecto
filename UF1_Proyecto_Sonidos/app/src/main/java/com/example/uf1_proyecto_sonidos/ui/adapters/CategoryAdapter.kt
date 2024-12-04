package com.example.uf1_proyecto_sonidos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uf1_proyecto_sonidos.R
import com.example.uf1_proyecto_sonidos.data.database.entities.Category

class CategoryAdapter(private val categoryList: MutableList<Category>) : RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryRow.text = category.name  // Assuming the 'Category' class has a 'name' property
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}
