package com.cojayero.dogedex3.doglist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cojayero.dogedex3.DogListViewModel
import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.api.ApiResponseStatus
import com.cojayero.dogedex3.databinding.ActivityDogListBinding
import com.cojayero.dogedex3.dogdetail.DogDetailActivity
import com.cojayero.dogedex3.dogdetail.DogDetailActivity.Companion.DOG_KEY
import com.cojayero.dogedex3.dogdetail.DogDetailComposeActivity

private val TAG = DogListActivity::class.java.simpleName
private const val GRID_LAYOUT_COLUMNS = 3
class DogListActivity : AppCompatActivity() {
    private val dogListViewModel: DogListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler = binding.dogRecycler
        val loadingWheel = binding.loadingWheel
        recycler.layoutManager = GridLayoutManager(this, GRID_LAYOUT_COLUMNS)
        val adapter = DogAdapter()
        /**
         * Añadimos el listener para cuando hagamos click en el item
         */
        adapter.setOnItemClickListener {
            //Paso el dog a DogDetail activity
            val intent = Intent(
                this,
                DogDetailComposeActivity::class.java
            )
            Log.d(TAG, "selected Dog $it")
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)

        }
        adapter.setOnLongItemClickListener {
            Log.d(TAG,"~~~~~~~~~~~~~~~~~OnLongClick $it")
            dogListViewModel.addDogToUser(it.id)
        }


        recycler.adapter = adapter
        dogListViewModel.dogList.observe(this) { dogList ->
            Log.d("Test", dogList.toString())
            adapter.submitList(dogList)
        }
        dogListViewModel.status.observe(this) { status ->
           when(status){
               is ApiResponseStatus.Error -> {
                loadingWheel.visibility = View.GONE
                   Toast.makeText(this,status.messageId,Toast.LENGTH_LONG).show()
               }
               is ApiResponseStatus.Loading -> loadingWheel.visibility = View.VISIBLE
               is ApiResponseStatus.Success -> {
                   loadingWheel.visibility = View.GONE

               }
           }
        }

    }


}