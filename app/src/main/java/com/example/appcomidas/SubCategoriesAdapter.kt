package com.example.appcomidas
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SubCategoriesAdapter(
    private val subCategories: MutableList<SubCategory>,
    private val context: Context,
    private val onItemClick: (SubCategory) -> Unit
) : RecyclerView.Adapter<SubCategoriesAdapter.SubCategoryViewHolder>() {

    inner class SubCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textSubCategoryName)
        val imageViewIcon: ImageView = itemView.findViewById(R.id.imageViewComidaIcon)
        val btnPlayVideo: ImageButton = itemView.findViewById(R.id.btnPlayVideo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_subcategory, parent, false)
        return SubCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        val subCategory = subCategories[position]
        val name = subCategory.name
        holder.textViewName.text = name

        // Aquí obtenemos el código de la imagen desde el objeto subCategory
        val imageCode = subCategory.imageCode

        // DEBUG: Imprimir el código de la imagen para verificar que es correcto
        Log.d("SubCategoriesAdapter", "ImageCode: $imageCode")

        // Intentamos obtener el identificador de recurso de la imagen
        val imageResourceId = context.resources.getIdentifier(imageCode, "drawable", context.packageName)

        // DEBUG: Imprimir el ID del recurso de imagen para verificar que es válido
        Log.d("SubCategoriesAdapter", "ImageResourceID: $imageResourceId")

        // Si el recurso de la imagen es válido, lo asignamos al ImageView
        if (imageResourceId != 0) {
            holder.imageViewIcon.setImageResource(imageResourceId)
        } else {
            // Si no se encuentra la imagen, asignamos una imagen predeterminada
            holder.imageViewIcon.setImageResource(R.drawable.logo)
        }

        // Configuramos el clic en el botón de video
        holder.btnPlayVideo.setOnClickListener {
            onItemClick(subCategory)
        }
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }
}
