package com.cojayero.dogedex3.dogdetail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.cojayero.dogedex3.Dog
import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.api.ApiResponseStatus
import com.cojayero.dogedex3.databinding.ActivityDogDetailBinding

private val TAG = DogDetailActivity::class.java.simpleName
class DogDetailActivity : AppCompatActivity() {
    companion object {
        const val DOG_KEY = "dog"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }
    private val viewModel  by viewModels<DogDetailViewModel>()
    private lateinit var loadingWheel :ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogDetailBinding.inflate(layoutInflater)
        loadingWheel = binding.loadingWheel
        setContentView(binding.root)
        val debugIntent = intent
        val bundle = intent.extras
        if (bundle != null) {
            for (key in bundle.keySet()) {
                Log.e(TAG, "<"+ key + ">: " + if (bundle[key] != null) bundle[key] else "NULL")
            }
        }
        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
        val isRecognition = intent?.extras?.getBoolean(IS_RECOGNITION_KEY,false)?: false

        Log.d(TAG,"dog" + dog.toString())
        if(dog == null){
            Toast.makeText(this,
                 getString(R.string.error_showing_dog_not_found),
                Toast.LENGTH_LONG).show()
            finish()
            return
        }
        binding.dogIndex.text = getString(R.string.dog_index_format, dog.index)
        binding.lifeExpectancy.text = getString(R.string.dog_life_expectancy_format, dog.lifeExpectancy)
        binding.dog = dog
        binding.dogImage.load(dog.imageUrl)
        binding.closeButton.setOnClickListener {
            if(isRecognition) {
                viewModel.addDogToUser(dog.id)
                // actualiza en  el backend el perro y cuando acaba cierra la vista con el observe status
            } else {
                finish()
            }
        }
        viewModel.status.observe(this) { status ->
            when(status){
                is ApiResponseStatus.Error -> {
                    loadingWheel.visibility = View.GONE
                    Toast.makeText(this,status.messageId,Toast.LENGTH_LONG).show()
                }
                is ApiResponseStatus.Loading -> loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> {
                    loadingWheel.visibility = View.GONE
                    finish() // finaliza la actividad
                }
            }
        }

    }
}