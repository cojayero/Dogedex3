package com.cojayero.dogedex3.doglist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cojayero.dogedex3.Dog
import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.databinding.DogListItemBinding
private val TAG = DogAdapter::class.java.simpleName
class DogAdapter : ListAdapter<Dog, DogAdapter.DogViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Dog>() {
        override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return oldItem.id == newItem.id
        }
    }

    // Añadimos un event listener en el adapter, para que cuando un elemenot se seleccione se use.
    private var onItemClickListener:((Dog)-> Unit)? = null
    fun setOnItemClickListener(onItemClickListener: (Dog)->Unit){
        this.onItemClickListener = onItemClickListener
    }
    // Añadimos un event listener en el adapter, para que cuando un elemenot se seleccione
    // con pulsacion larga.
    private var onLongItemClickListener:((Dog)-> Unit)? = null
    fun setOnLongItemClickListener(onLongItemClickListener: (Dog)->Unit){
        Log.d(TAG,"---->setOnLongItemClickListener")
        this.onLongItemClickListener = onLongItemClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val binding = DogListItemBinding.inflate(LayoutInflater.from(parent.context))
        return DogViewHolder(binding)
    }


    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = getItem(position)
        holder.bind(dog)

    }

    inner class DogViewHolder(val binding: DogListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dog: Dog) {

           if(dog.inCollection) {
               binding.dogImage.visibility = View.VISIBLE
               binding.dogName.visibility = View.GONE
               binding.dogListItemLayout.background = ContextCompat.getDrawable(
                   binding.dogImage.context,
                   R.drawable.dog_list_item_background
               )

               // binding.dogName.text = dog.name
               /**
                * Aqui llamamos al listener que hemos creado, asociandolo a un elemento de la vista
                * que presentamos.
                */
               binding.dogListItemLayout.setOnClickListener {
                   onItemClickListener?.invoke(dog)
               }
               binding.dogListItemLayout.setOnLongClickListener {
                   onLongItemClickListener?.invoke(dog)
                   true
               }
               binding.dogImage.load(dog.imageUrl)
           } else {
               binding.dogName.visibility = View.VISIBLE
               binding.dogName.text = dog.id.toString()
               binding.dogImage.visibility = View.GONE
               binding.dogListItemLayout.background = ContextCompat.getDrawable(
                   binding.dogImage.context,
                   R.drawable.dog_list_item_null_background
               )
           }


        }
    }


}