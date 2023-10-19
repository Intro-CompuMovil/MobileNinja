package com.example.proyectoparkninja

import android.Manifest
import android.app.Activity
import androidx.core.content.FileProvider
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditarPerfil : AppCompatActivity() {
    private lateinit var subirFoto: Button
    private lateinit var tomarFoto: Button
    private lateinit var imageView: ImageView
    private lateinit var takePicture: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var currentPhotoPath: String
    private val permissionsManager by lazy { PermissionsManager(this) }
    private var Camera_Permission_Code=10
    private var Storage_Permission_Code=20
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        subirFoto = findViewById(R.id.subirFotoBtn)
        tomarFoto = findViewById(R.id.tomarFotoBtn)
        imageView = findViewById(R.id.imgBtnfotoPerfil)

        tomarFoto.setOnClickListener {
            if (!permissionsManager.checkPermission(Manifest.permission.CAMERA)) {
                if (permissionsManager.shouldShowRequestPermissionRationale(PermissionsManager.CAMERA_PERMISSION)) {
                    showPermissionRationaleDialogCamara()
                }
                permissionsManager.requestPermission(Manifest.permission.CAMERA, Camera_Permission_Code)
            }
            else{
                openCamera()}
        }

        takePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(imageBitmap)
                val imageFile = createImageFile()

                try {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, "my_image.jpg")
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    }

                    val resolver = contentResolver
                    val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    val outputStream = resolver.openOutputStream(imageUri!!)
                    imageBitmap.compress(CompressFormat.JPEG, 100, outputStream)
                    outputStream?.close()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { selectedImageUri ->
                    // Maneja la imagen seleccionada desde la galería
                    imageView.setImageURI(selectedImageUri)
                }
            }
        }

        subirFoto.setOnClickListener {
            if (!permissionsManager.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (permissionsManager.shouldShowRequestPermissionRationale(PermissionsManager.STORAGE_PERMISSION)) {
                    showPermissionRationaleDialogStorage()
                }
                permissionsManager.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Storage_Permission_Code)
            }else{
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryLauncher.launch(galleryIntent)
            }

        }
    }
    private fun showPermissionRationaleDialogCamara() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Permiso de camara")
            .setMessage("Necesitamos acceso a la camara para tomar imagenes")
            .setPositiveButton("Permitir") { _, _ ->
                permissionsManager.requestPermission(PermissionsManager.CAMERA_PERMISSION, Camera_Permission_Code)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }
    private fun showPermissionRationaleDialogStorage() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Permiso de Almacenamiento")
            .setMessage("Necesitamos acceso al almacenamiento para tomar las imagenes de la galeria")
            .setPositiveButton("Permitir") { _, _ ->
                permissionsManager.requestPermission(PermissionsManager.STORAGE_PERMISSION, Storage_Permission_Code)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val imageFile = createImageFile()
            val imageUri = FileProvider.getUriForFile(this, "com.example.proyectoparkninja.fileprovider", imageFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            takePicture.launch(takePictureIntent)
        } else {
            Toast.makeText(this, "No se pudo abrir la cámara", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(storageDir, imageFileName)
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==Camera_Permission_Code){//nuestros permisos
            if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera()
            }else{
                //el permiso no ha sido aceptado
                Toast.makeText(this, "Permisos rechazado", Toast.LENGTH_SHORT).show()
            }
        }
        if(requestCode==Storage_Permission_Code){//nuestros permisos
            if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryLauncher.launch(galleryIntent)
            }else{
                //el permiso no ha sido aceptado
                Toast.makeText(this, "Permisos rechazado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}