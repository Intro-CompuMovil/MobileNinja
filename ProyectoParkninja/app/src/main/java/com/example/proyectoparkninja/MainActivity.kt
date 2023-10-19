package com.example.proyectoparkninja


import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonIngresar: Button = findViewById(R.id.botonIngresar)
        val intent1 = Intent(this, InicioActivity::class.java)
        val usuario:EditText = findViewById(R.id.correoLogin)
        val Password:EditText=findViewById(R.id.contreseñaLogin)
        val botonRegistrate: Button = findViewById(R.id.botonRegistrate)
        val intent2 = Intent(this, RegistrarseActivity::class.java)
        auth=FirebaseAuth.getInstance()

        botonIngresar.setOnClickListener() {
            val email=usuario.text.toString()
            val password=Password.text.toString()
            if(email.isEmpty() || password.isEmpty()){
                val toast=Toast.makeText(this,"Llene los datos",Toast.LENGTH_LONG).show()
            }
            else{
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Login completado.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            startActivity(intent1)

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(
                                this,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()

                        }
                    }

            }


        }

        botonRegistrate.setOnClickListener() {
            startActivity(intent2)
        }

    }

}