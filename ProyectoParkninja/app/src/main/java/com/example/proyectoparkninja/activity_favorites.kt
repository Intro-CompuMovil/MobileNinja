package com.example.proyectoparkninja

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class activity_favorites : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        // Nombres de parqueaderos de ejemplo
        val nombresParqueaderos = mutableListOf(
            "Parqueadero 1",
            "Parqueadero 2",
            "Parqueadero 3",
            "Parqueadero 4",
            "Parqueadero 5",
            "Parqueadero 6",
            "Parqueadero 7"
        )

        // Obtén una referencia al ListView
        val listView = findViewById<ListView>(R.id.listView)

        // Crea un ArrayAdapter para mostrar los nombres de parqueaderos
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresParqueaderos)

        // Asigna el adaptador al ListView
        listView.adapter = adapter

        // Configura el OnItemClickListener para el ListView
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { _, _, position, _ ->
            if (position in 0 until nombresParqueaderos.size) {
                val nombreParqueadero = nombresParqueaderos[position]
                // Muestra un Toast con el nombre del parqueadero seleccionado
                Toast.makeText(this, "Parqueadero seleccionado: $nombreParqueadero", Toast.LENGTH_SHORT).show()
                eliminarParqueadero(position, adapter, nombresParqueaderos)
            } else {
                // Muestra un Toast indicando que no se ha seleccionado un parqueadero
                Toast.makeText(this, "No se ha seleccionado un parqueadero", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun eliminarParqueadero(position: Int, adapter: ArrayAdapter<String>, nombresParqueaderos: MutableList<String>) {
        // Verifica si la posición está dentro de los límites de la lista
        if (position in 0 until nombresParqueaderos.size) {
            nombresParqueaderos.removeAt(position) // Elimina el parqueadero de la lista
            adapter.notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
        }
    }
}
