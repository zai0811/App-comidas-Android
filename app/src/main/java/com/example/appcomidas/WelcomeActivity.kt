package com.example.appcomidas
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class  WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val firstName = intent.getStringExtra("FIRST_NAME") ?: "Nombre"
        val lastName = intent.getStringExtra("LAST_NAME") ?: "Apellido"

        // Encuentra el TextView para el nombre y apellido y configura el texto
        val userTextView = findViewById<TextView>(R.id.user_text_vista)
        userTextView.text = getString(R.string.user_name_lastmane, firstName, lastName)

        // Encuentra el TextView para "BIENVENIDO" y deja el color como está configurado en el XML
        val welcomeTextView = findViewById<TextView>(R.id.inicio)
        // No es necesario cambiar el color aquí, ya está configurado en el XML
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener { view: View? ->
            val intent = Intent(this@WelcomeActivity, CategoryActivity::class.java)
            startActivity(intent)
        }
    }
}