package com.callbackequalsjack.myyelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.callbackequalsjack.myyelp.adapter.RestaurantAdapter
import com.callbackequalsjack.myyelp.api.RetrofitInstance
import com.callbackequalsjack.myyelp.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

const val defaultTerm = "Sushi"
const val defaultLocation = "Montreal"
const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var restaurantAdapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        retrieveData(defaultLocation, defaultTerm)
        setupSearchbar()

    }

    private fun setupRecyclerView() = binding.recyclerView.apply {
        restaurantAdapter = RestaurantAdapter()
        adapter = restaurantAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun retrieveData(location: String, term: String) {
        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getAll(location, term)
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {
                restaurantAdapter.restaurantList = response.body()!!.businesses
            } else {
                Log.e(TAG, "response not successful")
            }
        }
    }

    private fun setupSearchbar() {
        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    retrieveData(defaultLocation, query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }
}


