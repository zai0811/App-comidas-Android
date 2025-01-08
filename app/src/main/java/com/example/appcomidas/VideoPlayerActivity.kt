package com.example.appcomidas

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class VideoPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // Recibir el nombre de la subcategoría
        val subCategoryName = intent.getStringExtra("subCategoryName")

        // Mostrar el nombre de la subcategoría en el TextView
        val comidaTextView = findViewById<TextView>(R.id.comidaId)
        comidaTextView.text = subCategoryName  // Asignar el nombre de la subcategoría al TextView


        // Recibir el código de la imagen
        val imageCode = intent.getStringExtra("imageCode")
        val imageView = findViewById<ImageView>(R.id.imagenSubcategory)

        // Obtener el recurso de la imagen usando el código de la imagen
        val imageResourceId = resources.getIdentifier(imageCode, "drawable", packageName)
        if (imageResourceId != 0) {
            imageView.setImageResource(imageResourceId)  // Mostrar la imagen de la subcategoría
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_foreground)  // Imagen predeterminada si no se encuentra la imagen
        }

        val videoUrl = intent.getStringExtra("videoUrl")
        Log.d("VideoPlayerActivity", "Received Video URL: $videoUrl")

        // Convertir la URL de YouTube a su versión de incrustación (embed)
        val embedUrl = videoUrl?.replace("watch?v=", "embed/")

        // Verificar si el video URL es válido antes de cargarlo
        if (embedUrl != null && embedUrl.isNotEmpty()) {
            // Habilitar contenido mixto (http y https) en WebView
            val webView: WebView = findViewById(R.id.webView)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            }

            val videoHtml =
                "<iframe width=\"100%\" height=\"100%\" src=\"$embedUrl\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"

            webView.settings.javaScriptEnabled = true
            webView.webChromeClient = WebChromeClient()
            webView.loadData(videoHtml, "text/html", "utf-8")
        } else {
            Log.e("VideoPlayerActivity", "No video URL provided")
        }

        // Opción para abrir el video en un navegador o en la app de YouTube
        /*
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
        startActivity(intent)
        */

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
