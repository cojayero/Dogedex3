package com.cojayero.dogedex3.dogdetail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cojayero.dogedex3.Dog
import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.databinding.ActivityDogDetailBinding

private val TAG = DogDetailActivity::class.java.simpleName
class DogDetailActivity : AppCompatActivity() {
    companion object {
        const val DOG_KEY = "dog"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val debugIntent = intent
        val bundle = intent.extras
        if (bundle != null) {
            for (key in bundle.keySet()) {
                Log.e(TAG, "<"+ key + ">: " + if (bundle[key] != null) bundle[key] else "NULL")
            }
        }
        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
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
    }
}