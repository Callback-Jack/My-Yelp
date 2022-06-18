package com.callbackequalsjack.myyelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.callbackequalsjack.myyelp.adapter.FavoriteAdapter
import com.callbackequalsjack.myyelp.adapter.RestaurantAdapter
import com.callbackequalsjack.myyelp.database.Favorite
import com.callbackequalsjack.myyelp.database.FavoriteDao
import com.callbackequalsjack.myyelp.database.FavoriteDatabase
import com.callbackequalsjack.myyelp.databinding.ActivityFavoriteBinding
import com.callbackequalsjack.myyelp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.lang.Exception


class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var favoriteList: List<Favorite>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        favoriteAdapter = FavoriteAdapter() { position ->
            onItemClick(position)
        }
        favoriteDao = FavoriteDatabase.getInstance(this).favoriteDao
        setupRecyclerView()
        retrieveData()
    }

    private fun retrieveData(){
        lifecycleScope.launch {
            favoriteList = favoriteDao.getFavorites()
            if (favoriteList != null) {
                favoriteAdapter.favoriteList = favoriteList
            } else {
                Log.e("FavoriteActivity", "Database failure")
            }
        }
    }

    private fun setupRecyclerView(){
        binding.recyclerViewFavorites.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        retrieveData()
    }

    private fun onItemClick(position: Int) {
        val favorite = favoriteAdapter.favoriteList[position]
        createAlertDialog(favorite)
    }

    private fun createAlertDialog(favorite: Favorite) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete item from Favorite?")
        builder.setMessage("Do you want to delete this item from favorite?")
        builder.setPositiveButton("Yes"){ dialog, which -> deleteItemToFavorite(favorite)}
        builder.setNegativeButton("No"){ dialog, which -> Toast.makeText(this, "${favorite
            .itemTitle} not deleted.", Toast
            .LENGTH_SHORT).show()}
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun deleteItemToFavorite(favorite: Favorite) {
        lifecycleScope.launch {
            favoriteDao.deleteFavorite(favorite.itemTitle)
            Toast.makeText(this@FavoriteActivity, "${favorite
                .itemTitle} deleted from favorite",
                Toast.LENGTH_SHORT).show()
            val newList = favoriteDao.getFavorites()
            favoriteAdapter.favoriteList = newList
        }
    }
}