package com.example.proyectoparkninja

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class ParqueaderosCercanosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parqueaderos_cercanos)

        val botonPrecios: Button = findViewById(R.id.botonPrecios)
        val intent = Intent(this, PreciosParqueaderosCercanosActivity::class.java)

        botonPrecios.setOnClickListener() {
            startActivity(intent)
        }

        val botonTransParqueadero : Button = findViewById(R.id.botonTransParqueadero)
        val intent1 = Intent(this, InfoParqueaderoActivity::class.java)

        botonTransParqueadero.setOnClickListener(){
            startActivity(intent1)
        }

    }

}

