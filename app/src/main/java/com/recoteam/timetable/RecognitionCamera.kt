package com.recoteam.timetable

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.util.Log
import android.widget.TextView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class RecognitionCamera : Camera() {
    private lateinit var recognizedText : String
    private var audienceNumber : String = ""
    fun decodeBitmap(file: File): Bitmap {
        // First, decode EXIF data and retrieve transformation matrix
        val exif = ExifInterface(file.absolutePath)
        val transformation = decodeExifOrientation(
            exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_ROTATE_90
            )
        )


        // Read bitmap using factory methods, and transform it using EXIF data
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        return Bitmap.createBitmap(
            BitmapFactory.decodeFile(file.absolutePath),
            0,
            0,
            bitmap.width,
            bitmap.height,
            transformation,
            true
        )
    }
    fun startRecognizing(bmp : Bitmap, recognized_text_view : TextView) :String {
        audienceNumber = ""
        recognizedText = ""
        val image = FirebaseVisionImage.fromBitmap(bmp)
        val detector = FirebaseVision.getInstance()
            .onDeviceTextRecognizer

        val result = detector.processImage(image)
            .addOnSuccessListener { result ->
                recognizedText = result.text
                Log.v("Recognized:", recognizedText)
                processResultText()
                recognized_text_view.text =  audienceNumber
            }
            .addOnFailureListener {

            }
        return  audienceNumber
    }
    fun processResultText() {
        audienceNumber = ""
        var counter = 0

        for(i in recognizedText)
        {
            if (i.equals('-')) {
                audienceNumber += recognizedText[counter-2]
                audienceNumber += recognizedText[counter-1]
                audienceNumber += recognizedText[counter]
                audienceNumber += recognizedText[counter+1]
                audienceNumber += recognizedText[counter+2]
                audienceNumber += recognizedText[counter+3]
                break
            }
            counter++
        }
    }
}