package com.example.proyectoparkninja
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsManager(private val activity: Activity) {

    companion object {
        const val STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
        const val LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
    }

    fun checkPermission(permission: String): Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(activity, permission)
        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

    fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}
