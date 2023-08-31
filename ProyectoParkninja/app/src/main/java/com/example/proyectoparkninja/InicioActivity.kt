package com.example.proyectoparkninja

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val buscarBtn : ImageButton = findViewById(R.id.imgBtnBuscar)
        val subirFoto:Button =findViewById(R.id.subirFotoBtn)
        val tomarFoto:Button = findViewById(R.id.tomarFotoBtn)
        val intent1 = Intent(this, ParqueaderosCercanosActivity::class.java)
        val intent2 = Intent(this, ParqueaderosCercanosActivity::class.java)

        tomarFoto.setOnClickListener(){
            checkPermissions()
        }

        subirFoto.setOnClickListener(){
            requestStoragePermission()
        }

        buscarBtn.setOnClickListener(){
            requestLocationPermission()
            startActivity(intent1)
        }


    }

    //permisos de ubicacion
    val LOCATION_PERMISSION_REQUEST_CODE=1
    val STORAGE_PERMISSION_REQUEST_CODE = 1

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    //permisos camara
    private fun checkPermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            //permiso no aceptado por el momento
            requestCameraPermission()
        }else{
            //abrir camara
            openCamera()
        }
    }

    private fun openCamera() {
        Toast.makeText(this, "Abriendo Cámara", Toast.LENGTH_SHORT).show()
    }

    private fun requestCameraPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            //El usuario ya ha rechazado los permisos
            Toast.makeText(this, "Permisos rechazados", Toast.LENGTH_SHORT).show()

        }else {
            //pedir permiso, porque aun no los ha rechazado
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),15)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==15){//nuestros permisos
            if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera()
            }else{
                //el permiso no ha sido aceptado
                Toast.makeText(this, "Permisos rechazados por primera vez", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //permisos almacenamiento
    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {


        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }


}