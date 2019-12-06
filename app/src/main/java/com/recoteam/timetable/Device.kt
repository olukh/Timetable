package com.recoteam.timetable

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.view.TextureView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.recoteam.timetable.MainActivity

private val REQUEST_CODE_PERMISSIONS = 10
private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

abstract class Device()
{
    protected fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray, context: Context) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted(context)) {

            } else {

            }
        }
    }
    protected fun allPermissionsGranted(context : Context): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}
