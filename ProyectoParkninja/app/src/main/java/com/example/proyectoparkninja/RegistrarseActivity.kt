package com.example.proyectoparkninja

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class RegistrarseActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var Nombre:EditText
    lateinit var Correo:EditText
    lateinit var Contraseña:EditText
    lateinit var Apellido:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)
        Nombre=findViewById(R.id.nombreEditText)
        Correo=findViewById(R.id.correoEditText)
        Contraseña=findViewById(R.id.contraseñaEditText)
        Apellido=findViewById(R.id.apellidoEditText)
        val botonCrearCuenta:Button = findViewById(R.id.botonCrearCuenta)
        val intent1 = Intent (this, MainActivity::class.java)
        auth=FirebaseAuth.getInstance()

        botonCrearCuenta.setOnClickListener(){
            val nombre = Nombre.text.toString()
            val correo = Correo.text.toString()
            val contraseña = Contraseña.text.toString()
            val apellido = Apellido.text.toString()

            // Comprueba si los campos están vacíos

            // Comprueba si los campos están vacíos
            if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || apellido.isEmpty()) {
                // Si al menos uno de los campos está vacío, muestra un Toast
                Toast.makeText(
                    applicationContext,
                    "Por favor, completa todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                auth.createUserWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Registro exitoso, muestra un mensaje de éxito y redirige
                            Toast.makeText(
                                this,
                                "Cuenta creada exitosamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(intent1)
                        } else {
                            // Si la autenticación falla, muestra un mensaje de error específico
                            val exception = task.exception
                            if (exception != null) {
                                Toast.makeText(
                                    this,
                                    "Error al crear la cuenta: ${exception.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            }

        }
    }
}