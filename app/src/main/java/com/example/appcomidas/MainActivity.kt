package com.example.appcomidas;


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcomidas.R.*;
import com.example.appcomidas.R.layout.activity_main


class MainActivity : AppCompatActivity(), OnCategoryClickListener  {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(activity_main)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.categoriesContainer)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadCategories()
    }

    private fun loadCategories() {
        val categories = mutableListOf<Category>()
        val cursor = dbHelper.getAllCategories()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("Id"))
            val imageCode = cursor.getString(cursor.getColumnIndexOrThrow("ImageCode"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
            categories.add(Category(id, imageCode, name))
        }
        cursor.close()

        categoriesAdapter = CategoriesAdapter(categories, this)
        recyclerView.adapter = categoriesAdapter
    }

    override fun onCategoryClicked(categoryId: Int, categoryName: String) {
        val intent = Intent(this, SubCategoryActivity::class.java)
        intent.putExtra("CATEGORY_ID", categoryId)  // Pasar el ID de la categoría
        intent.putExtra("CATEGORY_NAME", categoryName)  // Pasar el nombre de la categoría
        startActivity(intent)
    }

}