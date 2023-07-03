package com.android.example.scannerdocumente

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var optionSpinner: Spinner
    private var selectedOption: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        setupOptionSpinner()

        findViewById<Button>(R.id.captureButton).setOnClickListener {
            takePhoto()
        }


        cameraExecutor = Executors.newSingleThreadExecutor()



    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    private fun takePhoto() {

        val imageCapture = imageCapture ?: return


        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_TAKEN, currentDate)
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Scanari")
            }



        }


        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()




        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults){

                    val savedUri = output.savedUri ?: Uri.EMPTY

                    sendPictureToDecisionScreen(savedUri, name, currentDate, selectedOption)

                }
            }
        )

    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()


            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(findViewById<PreviewView>(R.id.viewFinder).surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()


            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {

                cameraProvider.unbindAll()


                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)


            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun setupOptionSpinner() {
        optionSpinner = findViewById(R.id.optionSpinner)
        val options = resources.getStringArray(R.array.options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        optionSpinner.adapter = adapter

        optionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                selectedOption = selectedItem
                Log.d(TAG, "Selected type of document for picture: $selectedOption")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@CameraActivity, "Alege tipul documentului", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendPictureToDecisionScreen(savedUri: Uri?, name: String, currentDate: String, selectedOption: String?){
        val uriConverter = Converters()
        val uriString = uriConverter.fromUri(savedUri)
        val pictureData = Picture(uriString, name, currentDate, selectedOption)
        val intent = Intent(this@CameraActivity, ImageShowActivity::class.java)
        intent.putExtra("imageUri", savedUri.toString())
        val bundle = Bundle()
        bundle.putParcelable("pictureData", pictureData)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }



    companion object {
        private const val TAG = "DocScan"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}