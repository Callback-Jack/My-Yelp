package com.callbackequalsjack.myyelp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callbackequalsjack.myyelp.data.Businesse
import com.callbackequalsjack.myyelp.database.Favorite
import com.callbackequalsjack.myyelp.databinding.ItemFavoriteBinding
import com.callbackequalsjack.myyelp.databinding.ItemRestaurantBinding

class FavoriteAdapter(private val onItemClick: (position: Int) -> Unit) : RecyclerView
.Adapter<FavoriteAdapter.FavoriteViewHolder> () {

    inner class FavoriteViewHolder (val binding: ItemFavoriteBinding, val onItemClick: (position:
                                                                                       Int) -> Unit) : RecyclerView
    .ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            onItemClick(adapterPosition)
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Favorite> () {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.itemTitle == newItem.itemTitle
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var favoriteList: List<Favorite>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        val favorite = favoriteList[position]
        holder.binding.apply {
            itemPosition.text = "${position + 1}"
            itemTitle.text = favorite.itemTitle
            itemRating.rating = favorite.itemRating
            itemCategory.text = favorite.itemCategory
            itemPhone.text = favorite.itemPhone
            itemAddress.text = favorite.itemAddress
        }
        Glide.with(holder.itemView).load(favorite.itemImage).into(holder.binding.itemImage)

    }

    override fun getItemCount() = favoriteList.size
}