package com.example.appcomidas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SubCategoryActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var subCategoriesAdapter: SubCategoriesAdapter

    private fun getSubCategoryName(subCategoryId: Int): String {
        return dbHelper.getSubCategoryNameById(subCategoryId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        val categoryId = intent.getIntExtra("CATEGORY_ID", -1)
        if (categoryId == -1) {
            finish()  // Cerrar esta actividad si no se encontró el CATEGORY_ID
            return
        }

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewSubCategories)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Usar la nueva función para obtener el nombre de la categoría
        val categoryName = dbHelper.getCategoryNameById(categoryId)
        val textSubCategory = findViewById<TextView>(R.id.textSubCategory)
        textSubCategory.text = categoryName  // Mostrar el nombre de la categoría en el TextView

        // Obtener el código de la imagen de la categoría
        val imageCode = dbHelper.getCategoryImageCodeById(categoryId)
        val imageView = findViewById<ImageView>(R.id.categoryImageView)

        // Obtener el recurso de la imagen usando el código de la imagen
        val imageResourceId = resources.getIdentifier(imageCode, "drawable", packageName)
        if (imageResourceId != 0) {
            imageView.setImageResource(imageResourceId)  // Mostrar la imagen de la categoría
        } else {
            imageView.setImageResource(R.drawable.logo)  // Imagen predeterminada si no se encuentra la imagen
        }
        loadSubCategories(categoryId)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun loadSubCategories(categoryId: Int) {
        val subCategories = mutableListOf<SubCategory>()
        val cursor = dbHelper.getSubCategoriesByCategoryId(categoryId)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("Id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
            val imageCode = cursor.getString(cursor.getColumnIndexOrThrow("ImageCode"))
            val videoURL = cursor.getString(cursor.getColumnIndexOrThrow("VideoURL"))
            // Log para verificar el videoURL obtenido
            Log.d("SubCategoryActivity", "Subcategory: $name, VideoURL: $videoURL")
            subCategories.add(SubCategory(id, name, imageCode, videoURL))
        }
        cursor.close()

        subCategoriesAdapter = SubCategoriesAdapter(subCategories, this) { subCategory ->
            val videoUrl = subCategory.videoURL
            Log.d("SubCategoryActivity", "Video URL: $videoUrl")  // Verificar el videoUrl
            val intent = Intent(this@SubCategoryActivity, VideoPlayerActivity::class.java)
            intent.putExtra("videoUrl", videoUrl)  // Pasar el videoUrl
            intent.putExtra("subCategoryName", subCategory.name)  // Pasar el nombre de la subcategoría
            intent.putExtra("imageCode", subCategory.imageCode)  // Pasar la imagen de la subcategoría
            startActivity(intent)
        }
        recyclerView.adapter = subCategoriesAdapter
    }


}