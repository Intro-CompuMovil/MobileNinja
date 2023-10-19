package com.example.proyectoparkninja

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class PreciosParqueaderosCercanosActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_precios_parqueaderos_cercanos)
        val parqueaderos = leerParqueaderos(this)
        val container = findViewById<LinearLayout>(R.id.container)

        // Iterar a través de los parqueaderos y mostrar los datos
        for (parqueadero in parqueaderos) {
            val nombre = parqueadero.nombre
            val precio = parqueadero.precio
            val cupos = parqueadero.cupos

            val textView = TextView(this)
            textView.text = "Nombre: $nombre\nPrecio: $precio\nCupos: $cupos\n"

            container.addView(textView)

            // Agregar un separador visual
            val separator = View(this)
            separator.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
            )
            separator.setBackgroundColor(Color.BLACK)
            container.addView(separator)
        }


    }
}
