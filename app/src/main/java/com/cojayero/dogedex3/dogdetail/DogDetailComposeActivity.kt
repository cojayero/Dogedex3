package com.cojayero.dogedex3.dogdetail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cojayero.dogedex3.Dog
import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.api.ApiResponseStatus
import com.cojayero.dogedex3.dogdetail.ui.theme.Dogedex3Theme

class DogDetailComposeActivity : ComponentActivity() {
    companion object {
        const val DOG_KEY = "dog"
        const val MOST_PROBABLE_DOGS_IDS = "most_probable_dogs_ids"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }

    private val viewModel: DogDetailComposeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
        val isRecognition =
            intent?.extras?.getBoolean(IS_RECOGNITION_KEY, false) ?: false
        if (dog == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        setContent {
            val status = viewModel.status
            if(status.value is ApiResponseStatus.Success){
                finish()
            } else {
                Dogedex3Theme {
                    DogDetailScreen(
                        dog = dog,
                        status = status.value,
                        onButtonClicked = { onButtonClicked(dog.id, isRecognition) },
                        onErrorDialogDismiss = ::resetAPIResponseStatus
                    )
                }
            }
        }
    }

    private fun resetAPIResponseStatus() {
       viewModel.resetAPIResponseStatus()
    }

    private fun onButtonClicked(
        dogId: Long,
        isRecognition:Boolean) {
        if(isRecognition){
            viewModel.addDogToUser(dogId)
        } else {
            finish()
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DogDetailComposeActivityPreview() {
    Dogedex3Theme {
        val dog = Dog(
            1L,
            78,
            "Pug",
            "Herding",
            70.0,
            77.4,
            "https://i.blogs.es/811b9c/istock-520131419/1366_2000.jpeg",
            "10-12",
            "Friendly, PlayFull",
            "5",
            "6"
        )
        DogDetailComposeActivity()
    }
}