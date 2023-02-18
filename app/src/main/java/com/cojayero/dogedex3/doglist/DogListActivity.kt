package com.cojayero.dogedex3.doglist

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cojayero.dogedex3.DogListViewModel
import com.cojayero.dogedex3.databinding.ActivityDogListBinding


class DogListActivity : AppCompatActivity() {
    private val dogListViewModel: DogListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler = binding.dogRecycler
        recycler.layoutManager = LinearLayoutManager(this)
        val adapter = DogAdapter()
        recycler.adapter = adapter
        dogListViewModel.dogList.observe(this){
            dogList ->
            Log.d("Test",dogList.toString())
            adapter.submitList(dogList)
        }


    }


}