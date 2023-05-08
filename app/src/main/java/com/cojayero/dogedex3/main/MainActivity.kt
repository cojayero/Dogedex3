package com.cojayero.dogedex3.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import com.cojayero.dogedex3.Dog
import com.cojayero.dogedex3.LABEL_PATH
import com.cojayero.dogedex3.MODEL_PATH
import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.api.ApiResponseStatus
import com.cojayero.dogedex3.api.ApiServiceInterceptor
import com.cojayero.dogedex3.auth.LoginActivity
import com.cojayero.dogedex3.auth.User
import com.cojayero.dogedex3.databinding.ActivityMainBinding
import com.cojayero.dogedex3.dogdetail.DogDetailComposeActivity
import com.cojayero.dogedex3.doglist.DogListComposeActivity
import com.cojayero.dogedex3.machinelearning.Classifier
import com.cojayero.dogedex3.machinelearning.DogRecognition
import com.cojayero.dogedex3.settings.SettingsActivity
import com.cojayero.dogedex3.wholeimage.WholeImageActivity
import org.tensorflow.lite.support.common.FileUtil
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    private val requestPermissionLauncher = registerForActivityResult(
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
                getString(R.string.camera_permission_rejected_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var classifier: Classifier
    private lateinit var loadingWheel: ProgressBar
    private var isCameraReady = false
    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        loadingWheel = binding.loadingWheelMain
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
            // Este método no hace falta porque hemos implementado el reconocimiento continuo en el setAnalizer
            Log.d(TAG, "Click camera")
            if (isCameraReady) {
                Log.d(TAG, "--Take photo")
                takePhoto()
            } else {
                Log.d(TAG, "--CAmera is not ready")
            }
        }
        viewModel.status.observe(this) { status ->
            when (status) {
                is ApiResponseStatus.Error -> {
                    loadingWheel.visibility = View.GONE
                    Toast.makeText(this, status.messageId, Toast.LENGTH_SHORT).show()
                }
                is ApiResponseStatus.Loading -> loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> loadingWheel.visibility = View.GONE
            }
        }
        viewModel.dog.observe(this) { dog ->
            if (dog != null) {
                openDogDetailActivity(dog)
            }
        }
        viewModel.dogRecognition.observe(this) {
            enabledTakePhotoButton(it)
        }
        requestCameraPermissions()
    }

    private fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this, DogDetailComposeActivity()::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog)
        intent.putExtra(DogDetailComposeActivity.IS_RECOGNITION_KEY, true)
        startActivity(intent)
    }

    private fun openDogListActivity() {
        startActivity(Intent(this, DogListComposeActivity::class.java))
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
                    this, Manifest.permission.CAMERA
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
                        }.setNegativeButton(android.R.string.cancel) { _, _ ->
                        }.show()
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
        if (::cameraExecutor.isInitialized) {
            cameraExecutor.shutdown()
        }

    }

    private fun setupCamera() {
        Log.d(TAG, "---->Inicializando cámara")
        binding.cameraPreview.post {
            //Esto se ejecuta una vez inicilizada la vista

            imageCapture =
                ImageCapture.Builder().setTargetRotation(binding.cameraPreview.display.rotation)
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
            //Añadimos la parte de image analysis
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
            imageAnalysis.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imageProxy ->
                viewModel.recognizeImage(imageProxy)
                //      imageProxy.close()  --> hay que cerrarlo en el thread
            })
            // .. Fin ImageAnalysis
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))

    }

    private fun enabledTakePhotoButton(dogRecognition: DogRecognition) {
        // usamos el alpha porque usar isEnabled requeriría tratarlo como una animación
        if (dogRecognition.confidence > 70.0) {
            binding.takePhotoFab.alpha = 1f // completamente visible
            binding.takePhotoFab.setOnClickListener {
                viewModel.getDogByMlId(dogRecognition.id)
            }
        } else {
            binding.takePhotoFab.alpha = 0.2f
            binding.takePhotoFab.setOnClickListener(null)
        }
    }


    private fun takePhoto() {

        // el metodo ya no se usa porque tenemos el reconocimiento continuo
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(getOutputPhotoFile()).build()
        imageCapture.takePicture(
            outputFileOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    // insert your code here.
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // insert your code here.
                    Log.d(TAG, "-------Image saved")
                    val photoUri = outputFileResults.savedUri
                    val bitmap = BitmapFactory.decodeFile(photoUri?.path)
                    /*
                    --> aqui no lo llamamos ya poryqe implmentamos en la cámara el reconocimienot
                    --> continuo.
                    val dogRecognition = classifier.recognizeImage(bitmap).first()
                    if (dogRecognition.id == "pakito") {
                        openWholeImageActivity(photoUri)
                    }
                    viewModel.getDogByMlId(dogRecognition.id)
                    */
                }
            })

    }

    override fun onStart() {
        super.onStart()
        viewModel.setupClassifier(
            FileUtil.loadMappedFile(this@MainActivity, MODEL_PATH),
            FileUtil.loadLabels(this@MainActivity, LABEL_PATH)
        )
    }

    private fun openWholeImageActivity(photoUri: Uri?) {
        val intent = Intent(this, WholeImageActivity::class.java)
        intent.putExtra(WholeImageActivity.PHOTO_URI_KEY, photoUri.toString())
        startActivity(intent)
    }

    private fun getOutputPhotoFile(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name) + ".jpg").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) {
            mediaDir
        } else {
            filesDir
        }
    }


}