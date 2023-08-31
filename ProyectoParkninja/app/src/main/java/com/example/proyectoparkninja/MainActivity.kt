package com.example.proyectoparkninja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonIngresar: Button = findViewById(R.id.botonIngresar)
        val intent1 = Intent (this, InicioActivity::class.java)

        val botonRegistrate: Button = findViewById(R.id.botonRegistrate)
        val intent2 = Intent (this, RegistrarseActivity::class.java)

        botonIngresar.setOnClickListener(){
            startActivity(intent1)
        }

        botonRegistrate.setOnClickListener(){
            startActivity(intent2)
        }

    }
}