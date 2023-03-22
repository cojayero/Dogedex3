package com.cojayero.dogedex3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.cojayero.dogedex3.api.ApiServiceInterceptor
import com.cojayero.dogedex3.auth.LoginActivity
import com.cojayero.dogedex3.auth.User
import com.cojayero.dogedex3.databinding.ActivityMainBinding
import com.cojayero.dogedex3.doglist.DogListActivity
import com.cojayero.dogedex3.settings.SettingsActivity
import com.cojayero.dogedex3.wholeimage.WholeImageActivity
import com.cojayero.dogedex3.wholeimage.WholeImageActivity.Companion.PHOTO_URI_KEY
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                //OPen Camera
                setupCamera()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Toast.makeText(
                    this,
                    "You need to accept camera permision to use camera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private var isCameraReady = false


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "..............Entrando en main")
        val user = User.getLoggedInUser(this)
        if (user == null) {
            openLoginActivity()
            return
        } else {
            ApiServiceInterceptor.setSessionToken(user.authenticationToken)
        }
        binding.settingsFab.setOnClickListener {
            openSettingsActivity()
        }
        binding.dogListFab.setOnClickListener {
            openDogListActivity()
        }

        binding.takePhotoFab.setOnClickListener {
            Log.d(TAG,"Click camera")
            if(isCameraReady) {
                Log.d(TAG,"--Take photo")
                takePhoto()
            } else {
                Log.d(TAG,"--CAmera is not ready")
            }
        }
        requestCameraPermissions()
    }

    private fun openDogListActivity() {
        startActivity(Intent(this, DogListActivity::class.java))
    }

    private fun openSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun requestCameraPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    setupCamera()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.title_request_camera_permission))
                        .setMessage(getString(R.string.request_permission_camera_rationale))
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                        .setNegativeButton(android.R.string.cancel) { _, _ ->
                        }
                        .show()
                }
                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }
        } else {
            // OpenCamera
            setupCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::cameraExecutor.isInitialized) {
            cameraExecutor.shutdown()
        }

    }
    private fun setupCamera() {
        Log.d(TAG, "---->Inicializando cámara")
        binding.cameraPreview.post {
            //Esto se ejecuta una vez inicilizada la vista

            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.cameraPreview.display.rotation)
                .build()
            cameraExecutor = Executors.newSingleThreadExecutor()
            isCameraReady = true
            startCamera()

        }


    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.bindToLifecycle(this, cameraSelector, preview,imageCapture)
        }, ContextCompat.getMainExecutor(this))

    }


    private fun takePhoto() {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutputPhotoFile()).build()
        imageCapture.takePicture(outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    // insert your code here.
                }
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // insert your code here.
                    Log.d(TAG, "-------Image saved")
                    val photoUri = outputFileResults.savedUri
                    openWholeImageActivity(photoUri)
                }
            })

    }

    private fun openWholeImageActivity(photoUri: Uri?) {
        val intent = Intent(this, WholeImageActivity::class.java)
        intent.putExtra(WholeImageActivity.PHOTO_URI_KEY, photoUri.toString())
        startActivity(intent)
    }

    private fun getOutputPhotoFile():File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it,resources.getString(R.string.app_name) + ".jpg").apply { mkdirs() }
        }
        return if (mediaDir !=null && mediaDir.exists()){
            mediaDir
        } else
        {
            filesDir
        }
    }
}