package com.example.proyectoparkninja

import android.app.Activity
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File
import org.json.JSONArray
import org.json.JSONObject

class Parqueadero(
    val id: Int,
    val latitud: Double,
    val longitud: Double,
    val nombre: String,
    val precio: Double,
    val cupos: Int,
    val favorito : Int

)

fun obtenerListaParqueaderos(): List<Parqueadero> {
    val listaParqueaderos = mutableListOf<Parqueadero>()

    // Agregar parqueaderos a la lista
    listaParqueaderos.add(Parqueadero(1, 4.6284875, -74.0672394, "par1", 23.0, 20,1))
    listaParqueaderos.add(Parqueadero(2, 4.6284875, -74.0672394, "par2", 14.2, 12,0))
    listaParqueaderos.add(Parqueadero(3, 4.6282095, -74.064525, "par3", 31.3, 11,1))
    listaParqueaderos.add(Parqueadero(4, 4.6280384, -74.0633019, "par4", 2.1, 4,0))
    listaParqueaderos.add(Parqueadero(5, 4.6252366, -74.0655657, "par5", 23.4, 2,1))
    listaParqueaderos.add(Parqueadero(6, 4.6241458, -74.0674647, "par6", 53.4, 0,1))
    listaParqueaderos.add(Parqueadero(7, 4.8241458, -74.0674647, "par7", 53.4, 0,0))

    return listaParqueaderos
}
fun guardarParqueaderos(context: Activity, parqueaderos: List<Parqueadero>) {
    val jsonArray = JSONArray()

    for (parqueadero in parqueaderos) {
        val parqueaderoJson = JSONObject().apply {
            put("id", parqueadero.id)
            put("nombre", parqueadero.nombre)
            put("precio", parqueadero.precio)
            put("cupos", parqueadero.cupos)
            put("latitud", parqueadero.latitud)
            put("longitud", parqueadero.longitud)
            put("Favoritos",parqueadero.favorito)
        }
        jsonArray.put(parqueaderoJson)
    }

    val jsonText = jsonArray.toString()

    val file = File(context.filesDir, "parqueaderos.json")
    file.writeText(jsonText)
}
fun leerParqueaderos(context: Activity): List<Parqueadero> {
    val file = File(context.filesDir, "parqueaderos.json")

    if (file.exists()) {
        val jsonText = file.readText()
        val jsonArray = JSONArray(jsonText)
        val parqueaderos = mutableListOf<Parqueadero>()

        for (i in 0 until jsonArray.length()) {
            val parqueaderoJson = jsonArray.getJSONObject(i)
            val parqueadero = Parqueadero(
                parqueaderoJson.getInt("id"),
                parqueaderoJson.getDouble("latitud"),
                parqueaderoJson.getDouble("longitud"),
                parqueaderoJson.getString("nombre"),
                parqueaderoJson.getDouble("precio"),
                parqueaderoJson.getInt("cupos"),
                parqueaderoJson.getInt("Favoritos")

                )
            parqueaderos.add(parqueadero)
        }

        return parqueaderos
    } else {
        return emptyList()
    }

}
fun agregarMarcadoresEnMapa(mapa: GoogleMap, parqueaderos: List<Parqueadero>,context: Activity) {
    for (parqueadero in parqueaderos) {
        val latitud = parqueadero.latitud
        val longitud = parqueadero.longitud
        val nombre = parqueadero.nombre
        val precio = parqueadero.precio
        val cupos = parqueadero.cupos
        val favorito = parqueadero.favorito

        // Verificar si se debe mostrar el parqueadero en el mapa
        if (favorito==0 && cupos == 0) {
            // No mostrar parqueadero en el mapa
            continue
        }

        val ubicacion = LatLng(latitud, longitud)

        val markerOptions = MarkerOptions()
            .position(ubicacion)
            .title(nombre)

        // Configurar el icono del marcador según las condiciones
        when {
            favorito==1 && cupos > 0 -> {
                // Parqueadero favorito y con cupos disponibles (verde)
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            }
            favorito==1 && cupos == 0 -> {
                // Parqueadero favorito pero sin cupos (rojo)
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            }
            favorito==0 && cupos > 0 -> {
                // Parqueadero no favorito pero con cupos disponibles (azul)
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            }
        }

        val marker = mapa.addMarker(markerOptions)
        if (marker != null) {
            marker.tag = parqueadero
        }  // Guardar el objeto parqueadero en el marcador para usarlo después

        mapa.setOnInfoWindowClickListener { marker ->
            val parqueadero = marker.tag as Parqueadero
            if (parqueadero != null) {
                // Muestra información adicional al hacer clic en el marcador
                val info = "Nombre: ${parqueadero.nombre}\nPrecio: ${parqueadero.precio}\nCupos: ${parqueadero.cupos}"
                Toast.makeText(context, info, Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

}
