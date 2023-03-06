package com.cojayero.dogedex3.doglist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cojayero.dogedex3.Dog
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

           // binding.dogName.text = dog.name
            /**
             * Aqui llamamos al listener que hemos creado, asociandolo a un elemento de la vista
             * que presentamos.
             */
            binding.dogListItemLayout.setOnClickListener {
                onItemClickListener?.invoke(dog)
            }
            binding.dogImage.load(dog.imageUrl)
        }
    }


}