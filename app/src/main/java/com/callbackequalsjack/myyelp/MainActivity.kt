package com.callbackequalsjack.myyelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.callbackequalsjack.myyelp.adapter.RestaurantAdapter
import com.callbackequalsjack.myyelp.api.RetrofitInstance
import com.callbackequalsjack.myyelp.data.Businesse
import com.callbackequalsjack.myyelp.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

const val defaultTerm = "Sushi"
const val defaultLocation = "Montreal"
const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var spinner: Spinner
    private lateinit var searchBar: SearchView
    private lateinit var restaurantList: List<Businesse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.spinner
        searchBar = binding.searchBar
        restaurantAdapter = RestaurantAdapter()

        setupRecyclerView()
        retrieveData(defaultLocation, defaultTerm)
        setupSearchbar()

    }

    private fun setupRecyclerView() = binding.recyclerView.apply {
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
                restaurantList = response.body()!!.businesses
                restaurantAdapter.restaurantList = restaurantList
                setupSpinner()
            } else {
                Log.e(TAG, "response not successful")
            }
        }
    }

    private fun setupSearchbar() {
        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
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

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(this, R.array.spinner_options, android.R.layout
            .simple_spinner_item).also {
                adapter -> adapter.setDropDownViewResource(android.R.layout
            .simple_spinner_dropdown_item)

            binding.spinner.adapter = adapter
        }
        binding.spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selection = parent?.getItemAtPosition(position).toString()
        when (selection) {
            "Rating" -> sortByRating()
            "Price" -> sortByPrice()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun sortByRating() {
        val newData = restaurantList.sortedByDescending { item -> item.rating ?: 0.0 }
        restaurantAdapter.restaurantList = newData
    }

    private fun sortByPrice() {
        val newData = restaurantList.sortedByDescending { item -> item.price?.length ?: 0 }
        restaurantAdapter.restaurantList = newData
    }
}


