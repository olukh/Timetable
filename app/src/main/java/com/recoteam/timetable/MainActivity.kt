package com.recoteam.timetable

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.util.DisplayMetrics
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Rational
import android.util.Size
import android.view.WindowManager
import android.view.Display
import android.widget.TextView
import androidx.camera.core.*

import androidx.lifecycle.LifecycleOwner


class MainActivity : AppCompatActivity() {
    var camera = RecognitionCamera()
    lateinit var capture: ImageCapture
    private lateinit var photoFullPath : String
    lateinit var cam: Unit
    lateinit var lifecycleOwner: LifecycleOwner
    protected lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textureView: TextureView = findViewById(R.id.view_finder)
        val recognizedTextView : TextView = findViewById(R.id.recognized_text_view)
        val displayMetrics = resources.displayMetrics
        val orientation = this.resources.configuration.orientation
        lifecycleOwner = this as LifecycleOwner
        capture = camera.bindCamera(textureView, displayMetrics, orientation, lifecycleOwner)

        // Takes an images and saves it in the local storage
        capture_button?.setOnClickListener {
            val file = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
          capture?.takePicture(file, object : ImageCapture.OnImageSavedListener {

                override fun onError(useCaseError: ImageCapture.UseCaseError, message: String, cause: Throwable?) {
                    val msg = "Photo capture failed: $message"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.e("CameraXApp", msg)
                    cause?.printStackTrace()
                }

                override fun onImageSaved(file: File) {
                    val msg = "Photo capture succeeded: ${file.absolutePath}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d("CameraXApp", msg)
                    photoFullPath = file.absolutePath.toString()
                    bitmap = camera.decodeBitmap(file)

                    Log.v("Recognized text: ",  camera.startRecognizing(bitmap, recognizedTextView))
                }
            })
        }
    }
//    override fun bindCamera(textureView: TextureView, displayMetrics: DisplayMetrics, orientation: Int){
//        viewFinder = textureView
//        CameraX.unbindAll()
//        val metrics = displayMetrics
//
//        val screenAspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)
//        // Preview config for the camera
//        val previewConfig = PreviewConfig.Builder()
//            .setLensFacing(lensFacing)
//            .setTargetAspectRatio(screenAspectRatio)
//            .build()
//        val preview = Preview(previewConfig)
//        // Handles the output data of the camera
//        preview.setOnPreviewOutputUpdateListener { previewOutput ->
//            // Displays the camera image in our preview view
//            viewFinder.surfaceTexture = previewOutput.surfaceTexture
//        }
//        // Image capture config which controls the Flash and Lens
//        val imageCaptureConfig = ImageCaptureConfig.Builder()
//            .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
//            .setTargetRotation(orientation)
//            .setLensFacing(lensFacing)
//            .setFlashMode(FlashMode.OFF)
//            .setTargetResolution(Size(1440,720))
//            .build()
//
//        imageCapture = ImageCapture(imageCaptureConfig)
//
//        // Bind the camera to the lifecycle
//        CameraX.bindToLifecycle(this as LifecycleOwner, imageCapture, preview)
//    }
}
