package com.example.proyectoparkninja

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException


class ParqueaderosCercanosActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
     private val REQUEST_ENABLE_GPS = 123
    val javeriana= LatLng(4.6284875,-74.0672394)
    private lateinit var parqueaderos:List<Parqueadero>
    private lateinit var auth: FirebaseAuth

    private lateinit var googleMap: GoogleMap



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parqueaderos_cercanos)
        val listaParqueaderos = obtenerListaParqueaderos() // Supongamos que tienes una lista de parqueaderos
        guardarParqueaderos(this, listaParqueaderos)

        if (!isGPSEnabled()) {
            // Si el GPS no está habilitado, muestra un diálogo para permitir que el usuario lo active
            showEnableGPSDialog()
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val botonPrecios: Button = findViewById(R.id.botonPrecios)
        val Lugar=intent.getStringExtra("Lugar")

        mapFragment.getMapAsync { map ->
            googleMap = map
            if (Lugar != null) {
                centerMapToAddress(Lugar)}}




        botonPrecios.setOnClickListener() {
            intent = Intent(this, PreciosParqueaderosCercanosActivity::class.java)
            startActivity(intent)
        }


    }


    private fun centerMapToAddress(address: String) {
        val geocoder = Geocoder(this)
        try {
            val locationList = geocoder.getFromLocationName(address, 1)
            if (locationList != null && locationList.isNotEmpty()) {
                val latitude = locationList[0].latitude
                val longitude = locationList[0].longitude
                val location = LatLng(latitude, longitude)

                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 17f) // Ajusta el nivel de zoom según tus preferencias
                googleMap.moveCamera(cameraUpdate)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun isGPSEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun showEnableGPSDialog() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, REQUEST_ENABLE_GPS)
    }



    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        val parqueaderos = leerParqueaderos(this)
        agregarMarcadoresEnMapa(mMap, parqueaderos,this)


    }

}

