package com.callbackequalsjack.myyelp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.callbackequalsjack.myyelp.adapter.RestaurantAdapter
import com.callbackequalsjack.myyelp.api.RetrofitInstance
import com.callbackequalsjack.myyelp.data.Businesse
import com.callbackequalsjack.myyelp.database.Favorite
import com.callbackequalsjack.myyelp.database.FavoriteDao
import com.callbackequalsjack.myyelp.database.FavoriteDatabase
import com.callbackequalsjack.myyelp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

const val defaultTerm = "Sushi"
const val defaultLocation = "Montreal"
const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var spinner: Spinner
    private lateinit var searchBar: androidx.appcompat.widget.SearchView
    private lateinit var restaurantList: List<Businesse>
    private lateinit var favoriteDao: FavoriteDao
//    if we need the toggle button to open the nav menu
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.spinner
        searchBar = binding.searchBar
        restaurantAdapter = RestaurantAdapter() {
                position -> onItemClick(position)
        }


        setupRecyclerView()
        retrieveData(defaultLocation, defaultTerm)
        setupSearchbar()

        favoriteDao = FavoriteDatabase.getInstance(this).favoriteDao

//        setup for the menu toggle
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        set listener for navigation
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item_search -> binding.drawerLayout.closeDrawer(binding.navView)
                R.id.menu_item_favorite -> returnToFavorite()
            }
            true
        }
    }

    private fun returnToFavorite() {
        val intent = Intent(this, FavoriteActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() = binding.recyclerView.apply {
        adapter = restaurantAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun onItemClick(position: Int) {
        val item: Businesse = restaurantAdapter.restaurantList[position]
        val favorite = Favorite(
            item.name,
            item.rating.toFloat(),
            item.categories[0].title,
            item.display_phone,
            "${item.location.address1}, " +
                    "${item.location.city}, " +
                    "${item.location.state}",
            item.image_url
        )
        createAlertDialog(favorite)
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
        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
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

    override fun onNothingSelected(parent: AdapterView<*>?) { }

    private fun sortByRating() {
        val sorted = restaurantList.sortedByDescending { item -> item.rating ?: 0.0 }
        restaurantAdapter.restaurantList = sorted
    }

    private fun sortByPrice() {
        val sorted = restaurantList.sortedByDescending { item -> item.price?.length ?: 0 }
        restaurantAdapter.restaurantList = sorted
    }

    private fun createAlertDialog(favorite: Favorite) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add to Favorite?")
        builder.setMessage("Do you want to add this item to favorite?")
        builder.setPositiveButton("Yes"){ dialog, which -> addItemToFavorite(favorite)}
        builder.setNegativeButton("No"){ dialog, which -> Toast.makeText(this, "${favorite
            .itemTitle} not added.", Toast
            .LENGTH_SHORT).show()}
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun addItemToFavorite(favorite: Favorite) {

        lifecycleScope.launch {
            favoriteDao.insertFavorite(favorite)
            Toast.makeText(this@MainActivity, "${favorite
                .itemTitle} added to favorite",
                Toast.LENGTH_SHORT).show()
        }
    }
}


