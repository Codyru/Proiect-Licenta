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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var tempPhotoFile: File? = null
    private lateinit var optionSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        optionSpinner = findViewById(R.id.optionSpinner)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
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
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_TAKEN, currentDate)
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }



        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()



        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){

                    val savedUri = output.savedUri ?: Uri.EMPTY

                    var selectedOption: String? = null
//                    val options = arrayOf("Buletin", "Diploma", "Pasaport")
//                    val adapter = ArrayAdapter(this@CameraActivity, android.R.layout.simple_spinner_item, options)
//                    optionSpinner.adapter = adapter
                    optionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectedItem = parent?.getItemAtPosition(position).toString()

                            // Use a when statement to set the variable based on the selected option
                            selectedOption = selectedItem

                            // Use the selectedVariable as needed
                            Log.d(TAG, "Selected type of document for picture: $selectedOption")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // Handle the case when nothing is selected (optional)
                        }
                    }

                    val pictureData = Picture(savedUri, name, currentDate, selectedOption)
                    val intent = Intent(this@CameraActivity, ImageShowActivity::class.java)
                    intent.putExtra("imageUri", savedUri.toString())
                    val bundle = Bundle()
                    bundle.putParcelable("pictureData", pictureData)
                    intent.putExtras(bundle)
                    startActivity(intent)
//                    Good code keep it just in case

//                    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
////                    val savedUri = output.savedUri ?: Uri.fromFile(tempPhotoFile)
////                    //val savedUri = output.savedUri ?: Uri.EMPTY
////                    val msg = "Photo capture succeeded: ${output.savedUri}"
////                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
////                    Log.d(TAG, msg)
//
////                    // Pass the URI to the next activity
////                    val intent = Intent(this@CameraActivity, ImageShowActivity::class.java)
////                    intent.putExtra("imageUri", savedUri.toString())
////                    startActivity(intent)
//
////                    Good code keep it just in case
//
//                    val image: InputImage
//                    try {
//                        image = InputImage.fromFilePath(this@CameraActivity, savedUri)
//                        recognizer.process(image).addOnSuccessListener {
//                                result->val resultText = result.text
//                            for (block in result.textBlocks) {
//                                val blockText = block.text
//                                val blockCornerPoints = block.cornerPoints
//                                val blockFrame = block.boundingBox
//                                for (line in block.lines) {
//                                    val lineText = line.text
//                                    val lineCornerPoints = line.cornerPoints
//                                    val lineFrame = line.boundingBox
//                                    for (element in line.elements) {
//                                        val elementText = element.text
//                                        val elementCornerPoints = element.cornerPoints
//                                        val elementFrame = element.boundingBox
//                                        Log.d("Testing recog result","Element: " + elementText)
//
//                                        if(elementText.equals("CNP"))
//                                            continue
//                                        if (validateCNP(elementText)){
//
//                                            Log.d("CNP_VALIDARE", "Validat sau nu")
//                                        }
//
//
//                                    }
//                                }
//                            }
//                        }
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }

                    val textRecognition = TextRecognition(this@CameraActivity)
                    textRecognition.recognizeText(savedUri) { resultText ->
                        val cnpValidator = Validator()
                        val lines = resultText.split("\n")
                        for (line in lines) {
                            val words = line.split(" ")
                            for (word in words) {
                                if (cnpValidator.validateCNP(word)) {
                                    Log.d("CNP_VALIDARE", "Este valid")
                                    Toast.makeText(this@CameraActivity, "CNP valid",  Toast.LENGTH_LONG)
                                }
                            }
                        }
                    }


                }
            }
        )

    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(findViewById<PreviewView>(R.id.viewFinder).surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
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


//    fun validateCNP(CNP:String):Boolean{
//        if (CNP.length != 13 || !CNP.matches("[0-9]+".toRegex())) {
//            return false
//        }
//
//        val controlDigit = CNP[12].toString().toIntOrNull()
//        if (controlDigit == null) {
//            return false
//        }
//
//        val coefficients = arrayOf(2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9)
//        var sum = 0
//        for (i in 0 until 12) {
//            val digit = CNP[i].toString().toIntOrNull()
//            if (digit == null) {
//                return false
//            }
//            sum += digit * coefficients[i]
//        }
//
//        val remainder = sum % 11
//        val controlDigitComputed = if (remainder == 10) 1 else remainder
//
//        return controlDigit == controlDigitComputed
//    }


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