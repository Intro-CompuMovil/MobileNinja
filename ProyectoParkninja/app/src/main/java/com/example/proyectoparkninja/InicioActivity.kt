package com.example.proyectoparkninja

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class InicioActivity : AppCompatActivity() {
    private val permissionsManager by lazy { PermissionsManager(this) }
    private lateinit var buscarBtn: ImageButton
    private lateinit var EdiPerfil: Button
    private lateinit var intent1: Intent
    private lateinit var intent2: Intent
    private lateinit var Lugar: EditText
    private lateinit var texto: TextView
    private lateinit var auth: FirebaseAuth
    private val Location_PERMISSION_Code = 3



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio) // Mueve esta línea al principio de onCreate
        texto = findViewById(R.id.textnombre) // Luego puedes acceder a 'textnombre'

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            texto.text = user.email
        }

        buscarBtn = findViewById(R.id.imgBtnBuscar)
        Lugar = findViewById(R.id.nombreEditText)
        EdiPerfil = findViewById(R.id.EditarPerfil)
        intent1 = Intent(this, ParqueaderosCercanosActivity::class.java)
        intent2 = Intent(this, EditarPerfil::class.java)



        buscarBtn.setOnClickListener(){
            if (!permissionsManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (permissionsManager.shouldShowRequestPermissionRationale(PermissionsManager.LOCATION_PERMISSION)) {
                    showPermissionRationaleDialogLocalizacion()
                }
                permissionsManager.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Location_PERMISSION_Code)
            }
            else{
                val ubicacion = Lugar.text.toString()
                if(ubicacion.isNotEmpty()){
                    intent1.putExtra("Lugar",ubicacion)
                    startActivity(intent1)
                }
                else{
                    val toast=Toast.makeText(this,"La direccion esta vacia porfavor llenala",Toast.LENGTH_LONG)
                    toast.show()
                }

            }
        }
        EdiPerfil.setOnClickListener(){
            startActivity(intent2)
        }


    }

    private fun showPermissionRationaleDialogLocalizacion() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Permiso de localizacion")
            .setMessage("Necesitamos acceso a la localizacion para mostrarte el mapa.")
            .setPositiveButton("Permitir") { _, _ ->
                permissionsManager.requestPermission(PermissionsManager.LOCATION_PERMISSION, Location_PERMISSION_Code)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==Location_PERMISSION_Code){//nuestros permisos
            if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivity(intent1)
            }else{
                //el permiso no ha sido aceptado
                Toast.makeText(this, "Permisos rechazados por primera vez", Toast.LENGTH_SHORT).show()
            }
        }
    }




}