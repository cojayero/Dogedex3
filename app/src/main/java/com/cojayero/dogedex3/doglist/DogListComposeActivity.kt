package com.cojayero.dogedex3.doglist

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.cojayero.dogedex3.Dog
import com.cojayero.dogedex3.DogListComposeViewModel
import com.cojayero.dogedex3.dogdetail.DogDetailComposeActivity
import com.cojayero.dogedex3.dogdetail.ui.theme.Dogedex3Theme


private val TAG = DogListComposeActivity::class.java.simpleName

class DogListComposeActivity : ComponentActivity() {
    private val viewModel: DogListComposeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dogList = viewModel.dogList
            Dogedex3Theme {
                DogListScreen(dogList = dogList.value,
                    onDogClicked = ::openDogDetailActivity
                )
            }
        }
    }

    private fun openDogDetailActivity(dog: Dog) {
        val intent = Intent(this,DogDetailComposeActivity::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY,dog   )
        startActivity(intent)
    }
}