package com.cojayero.dogedex3.wholeimage

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.databinding.ActivityWholeImageBinding
import java.io.File
private val  TAG = WholeImageActivity::class.java.simpleName
class WholeImageActivity : AppCompatActivity() {
    companion object {
        const val PHOTO_URI_KEY = "photo_uri"
    }

    private lateinit var binding: ActivityWholeImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWholeImageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val photoUri = intent.extras?.getString(PHOTO_URI_KEY) ?: ""
        Log.d(TAG,"----->URI : $photoUri")
        val uri = Uri.parse(photoUri)
        Log.d(TAG,"------>PATH: $uri")
        if (uri == null) {
            Toast.makeText(this, getString(R.string.error_showing_photo_uri), Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }
        binding.wholeImage.load(File(uri.path))

    }
}