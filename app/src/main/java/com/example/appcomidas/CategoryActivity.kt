package com.example.appcomidas;


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcomidas.R.layout.activity_category


class CategoryActivity : AppCompatActivity(), OnCategoryClickListener  {
    private lateinit var dbHelper: DatabaseHelper  // Instancia de DatabaseHelper para acceder a la base de datos.
    private lateinit var recyclerView: RecyclerView  // RecyclerView para mostrar las categorías.
    private lateinit var categoriesAdapter: CategoriesAdapter  // Adapter para manejar los datos y la visualización de las categorías en el RecyclerView.

    // Método onCreate que se llama al crear la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Habilita el diseño que se extiende hacia los bordes de la pantalla.
        setContentView(activity_category)  // Establece el layout de la actividad usando el layout definido en activity_main.

        dbHelper = DatabaseHelper(this)  // Inicializa dbHelper.
        recyclerView = findViewById(R.id.categoriesContainer)  // Encuentra el RecyclerView definido en el layout.
        recyclerView.layoutManager = LinearLayoutManager(this)  // Establece LinearLayoutManager como el administrador del layout para el RecyclerView.

        loadCategories()  // Carga las categorías desde la base de datos y las muestra en el RecyclerView.
    }

    // Método privado para cargar categorías de la base de datos y configurar el RecyclerView.
    private fun loadCategories() {
        val categories = mutableListOf<Category>()  // Lista mutable para almacenar los objetos Category.
        val cursor = dbHelper.getAllCategories()  // Obtiene todas las categorías de la base de datos.
        while (cursor.moveToNext()) {  // Itera a través de los resultados de la consulta.
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("Id"))  // Obtiene el ID de la categoría.
            val imageCode = cursor.getString(cursor.getColumnIndexOrThrow("ImageCode"))  // Obtiene el código de imagen de la categoría.
            val name = cursor.getString(cursor.getColumnIndexOrThrow("Name"))  // Obtiene el nombre de la categoría.
            categories.add(Category(id, imageCode, name))  // Añade un nuevo objeto Category a la lista.
        }
        cursor.close()  // Cierra el cursor.

        categoriesAdapter = CategoriesAdapter(categories, this)  // Crea un nuevo CategoriesAdapter con las categorías y un listener.
        recyclerView.adapter = categoriesAdapter  // Establece el adapter del RecyclerView.
    }

    // Método que maneja el evento de clic en una categoría.
    override fun onCategoryClicked(categoryId: Int, categoryName: String) {
        val intent = Intent(this, SubCategoryActivity::class.java)  // Crea un nuevo Intent para SubCategoryActivity.
        intent.putExtra("CATEGORY_ID", categoryId)  // Agrega el ID de la categoría al Intent.
        intent.putExtra("CATEGORY_NAME", categoryName)  // Agrega el nombre de la categoría al Intent.
        startActivity(intent)  // Inicia SubCategoryActivity.
    }

}