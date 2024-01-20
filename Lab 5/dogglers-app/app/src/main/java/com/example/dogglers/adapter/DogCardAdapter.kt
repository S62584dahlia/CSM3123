package com.example.dogglers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dogglers.R
import com.example.dogglers.data.DataSource
import com.example.dogglers.model.Dog

/**
 * Adapter to inflate the appropriate list item layout and populate the view with information
 * from the appropriate data source
 */
class DogCardAdapter(
    private val context: Context?,
    private val layout: Layout
): RecyclerView.Adapter<DogCardAdapter.DogCardViewHolder>() {

    private val dogData: List<Dog> = DataSource.dogs

    /**
     * Initialize view elements
     */
    class DogCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewDog: ImageView = view.findViewById(R.id.imageViewDog)
        val textViewName: TextView = view.findViewById(R.id.textViewDogName)
        val textViewAge: TextView = view.findViewById(R.id.textViewDogAge)
        val textViewHobbies: TextView = view.findViewById(R.id.textViewDogHobbies)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = when (layout) {
            Layout.GRID -> inflater.inflate(R.layout.grid_list_item, parent, false)
            else -> inflater.inflate(R.layout.vertical_horizontal_list_item, parent, false)
        }
        return DogCardViewHolder(view)
    }

    override fun getItemCount(): Int = dogData.size

    override fun onBindViewHolder(holder: DogCardViewHolder, position: Int) {
        val currentDog = dogData[position]

        // Set the image resource for the current dog
        holder.imageViewDog.setImageResource(R.drawable.bella)

        // Set the text for the current dog's name
        holder.textViewName.text = currentDog.name

        // Set the text for the current dog's age
        holder.textViewAge.text = context?.getString(R.string.dog_age, currentDog.age)

        // Set the text for the current dog's hobbies
        holder.textViewHobbies.text = context?.getString(R.string.dog_hobbies, currentDog.hobbies)
    }

    enum class Layout {
        GRID, VERTICAL_HORIZONTAL
    }
}
