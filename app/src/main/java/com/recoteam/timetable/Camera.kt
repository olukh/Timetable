package com.recoteam.timetable

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.Image
import android.util.DisplayMetrics
import android.util.Rational
import android.util.Size
import android.view.TextureView
import android.view.WindowManager
import androidx.camera.core.*
import androidx.lifecycle.LifecycleOwner

abstract class Camera : Device()
{
    protected lateinit var viewFinder: TextureView
    protected var lensFacing = CameraX.LensFacing.BACK
    protected lateinit var photoFullPath : String
    open fun bindCamera(textureView: TextureView, displayMetrics: DisplayMetrics, orientation: Int, lifecycleOwner: LifecycleOwner
    ) : ImageCapture {
        viewFinder = textureView
        CameraX.unbindAll()
        val metrics = displayMetrics

        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)
        // Preview config for the camera
        val previewConfig = PreviewConfig.Builder()
            .setLensFacing(lensFacing)
            .setTargetAspectRatio(screenAspectRatio)
            .build()
        val preview = Preview(previewConfig)
        // Handles the output data of the camera
        preview.setOnPreviewOutputUpdateListener { previewOutput ->
            // Displays the camera image in our preview view
            viewFinder.surfaceTexture = previewOutput.surfaceTexture
        }
        // Image capture config which controls the Flash and Lens
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            .setTargetRotation(orientation)
            .setLensFacing(lensFacing)
            .setFlashMode(FlashMode.OFF)
            .setTargetResolution(Size(1440,720))
            .build()

        var imageCapture = ImageCapture(imageCaptureConfig)
        // Bind the camera to the lifecycle
        CameraX.bindToLifecycle(lifecycleOwner, imageCapture, preview)
        return  imageCapture
    }
    protected fun decodeExifOrientation(orientation: Int): Matrix {
        val matrix = Matrix()

        // Apply transformation corresponding to declared EXIF orientation
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> Unit
            ExifInterface.ORIENTATION_UNDEFINED -> Unit
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270F)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1F, 1F)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1F, -1F)
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.postScale(-1F, 1F)
                matrix.postRotate(270F)
            }
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.postScale(-1F, 1F)
                matrix.postRotate(90F)
            }

            // Error out if the EXIF orientation is invalid
            else -> throw IllegalArgumentException("Invalid orientation: $orientation")
        }

        // Return the resulting matrix
        return matrix
    }
}