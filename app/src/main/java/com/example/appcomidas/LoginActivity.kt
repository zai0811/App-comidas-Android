package com.example.appcomidas
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appcomidas.R

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            // Verificar credenciales
            if (dbHelper.checkUserCredentials(username, password)) {
                val userInfo = dbHelper.getUserDetails(username)
                if (userInfo.moveToFirst()) {
                    val nameIndex = userInfo.getColumnIndex("Name")
                    val lastNameIndex = userInfo.getColumnIndex("LastName")
                    val firstName = userInfo.getString(nameIndex)
                    val lastName = userInfo.getString(lastNameIndex)
                    userInfo.close()
                    Toast.makeText(this, "Sesion Iniciada", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, WelcomeActivity::class.java).apply {
                        putExtra("FIRST_NAME", firstName)
                        putExtra("LAST_NAME", lastName)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    userInfo.close()
                    Toast.makeText(this, "Error al obtener detalles del usuario", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Este es el lugar correcto para el mensaje de error de credenciales incorrectas
                Toast.makeText(this, "Credenciales incorrectas, intenta de nuevo.", Toast.LENGTH_LONG).show()
            }
        }
    }
}