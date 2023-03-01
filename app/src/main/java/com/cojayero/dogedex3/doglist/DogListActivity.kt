package com.cojayero.dogedex3.doglist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cojayero.dogedex3.DogListViewModel
import com.cojayero.dogedex3.api.ApiResponseStatus
import com.cojayero.dogedex3.databinding.ActivityDogListBinding
import com.cojayero.dogedex3.dogdetail.DogDetailActivity
import com.cojayero.dogedex3.dogdetail.DogDetailActivity.Companion.DOG_KEY

private val  TAG = DogListActivity::class.java.simpleName
class DogListActivity : AppCompatActivity() {
    private val dogListViewModel: DogListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler = binding.dogRecycler
        recycler.layoutManager = LinearLayoutManager(this)
        val adapter = DogAdapter()
        /**
         * Añadimos el listener para cuando hagamos click en el item
         */
        adapter.setOnItemClickListener {
            //Paso el dog a DogDetail activity
            val intent = Intent(this,
                                DogDetailActivity::class.java)
            Log.d(TAG, "selected Dog $it")
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)

        }

        recycler.adapter = adapter
        dogListViewModel.dogList.observe(this) { dogList ->
            Log.d("Test", dogList.toString())
            adapter.submitList(dogList)
        }
dogListViewModel.status.observe(this){
    status -> when(status){
        ApiResponseStatus.LOADING -> {
            // Mostrar loading status
        }
        ApiResponseStatus.ERROR -> {
            Toast.makeText(this,"Hubo un error al descargar datos",Toast.LENGTH_SHORT).show()
        }
    ApiResponseStatus.SUCCESS -> {
        TODO()
    }
}
}

    }


}