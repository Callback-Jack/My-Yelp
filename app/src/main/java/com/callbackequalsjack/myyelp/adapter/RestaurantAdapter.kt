package com.callbackequalsjack.myyelp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callbackequalsjack.myyelp.data.Businesse
import com.callbackequalsjack.myyelp.databinding.ItemRestaurantBinding

class RestaurantAdapter : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> () {

    inner class RestaurantViewHolder (val binding: ItemRestaurantBinding) : RecyclerView
    .ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Businesse> () {
        override fun areItemsTheSame(oldItem: Businesse, newItem: Businesse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Businesse, newItem: Businesse): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var restaurantList: List<Businesse>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
            ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.binding.apply {
            itemPosition.text = "${position + 1}"
            itemTitle.text = restaurant.name
            itemRating.rating = restaurant.rating.toFloat()
            itemCategory.text = restaurant.categories[0].title
            itemPhone.text = restaurant.display_phone
            itemAddress.text = "${restaurant.location.address1}, " +
                    "${restaurant.location.city}, " +
                    "${restaurant.location.state}"
        }
        Glide.with(holder.itemView).load(restaurant.image_url).into(holder.binding.itemImage)

    }

    override fun getItemCount() = restaurantList.size
}