package com.example.appcomidas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appcomidas.R.*

// Adaptador para mostrar las categorías
class CategoriesAdapter(
    private val categories: List<Category>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    // ViewHolder que representa cada elemento en el RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val button: Button = itemView.findViewById(R.id.button)
        private val imageViewCategory: ImageView = itemView.findViewById(R.id.imageViewCategory)

        // Vincular datos a la vista
        fun bind(category: Category, listener: OnCategoryClickListener) {
            // Asignar el nombre de la categoría
            textView.text = category.name

            // Asignar el listener al botón, pasando tanto el ID como el nombre de la categoría
            button.setOnClickListener {
                listener.onCategoryClicked(category.id, category.name)
            }

            // Obtener el código de imagen y cargar la imagen
            val imageResourceId = itemView.context.resources.getIdentifier(
                category.imageCode, "drawable", itemView.context.packageName
            )

            // Asignar la imagen de la categoría si existe, de lo contrario una predeterminada
            if (imageResourceId != 0) {
                imageViewCategory.setImageResource(imageResourceId)
            } else {
                imageViewCategory.setImageResource(R.drawable.logo) // Imagen predeterminada
            }
        }
    }

    // Inflar el layout para cada elemento del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_block, parent, false)
        return ViewHolder(view)
    }

    // Enlazar el ViewHolder con los datos
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position], listener)

        // Configurar el margen entre elementos
        val marginInPixels = holder.itemView.resources.getDimensionPixelSize(R.dimen.recycler_item_spacing)
        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.bottomMargin = marginInPixels
        holder.itemView.layoutParams = layoutParams
    }

    // Obtener la cantidad de elementos en la lista
    override fun getItemCount(): Int = categories.size
}
