# My Yelp 
### This app let user view restaurants from Yelp, search them, sort them and save them as favorites  
  

These are the key steps for this project:
  
- Project setup:
  - Add dependencies for `Retrofit`, `Coroutines` and `Coroutine Lifecycle Scopes`.
  - Enable view binding.
  - setup user permission for Internet.
  
- Build the layout:
  - For the main activity, we need `SearchView`, `Spinner` and `RecyclerView`.
  - For the recycler item layout, we use `LinearLayout` to arrange the attributes.
  
- Connect to Yelp API:
  - First we need to register a project to get the api key.
  - We create the API `interface` using `Retrofit`.
  - We create a `Retrofit` instance singleton.
  - We customize the `RecyclerView` adapter using `DiffUtil`.
  - We create the data class by parsing the API's `JSON` response using the plugin for conversion.
  
- Display the result:
  - setup the binding in `MainActivity`.
  - setup the `RecyclerView` adapter.
  - use `lifecycleScope` to retrieve data from the API. 
  - catch the `IOException` and `HttpException`.
  
- Create the Search bar for user to search restaurants:
  - Create the view using `SearchView`.
  - Set the `setOnQueryTextListener` in the `SearchView`.
  - Override the functions: `onQueryTextSubmit(query: String?)` and `onQueryTextChange(newText: 
    String?)`. 
  
- Create some sorting methods for the restaurant list:
  - Extend the `MainActivity` with `AdapterView.OnItemSelectedListener`.
  - Setup the `spinner.adapter` with `R.array.spinner_options` which has the options to display.
  - Override the `onItemSelected()` with the callback functions implemented with the sorting 
    methods.

- Create database for storage of the user's favorite restaurants using `Room`:
  - Add `Room` dependencies to `build.gradle`.
  - Define `data class` `Favorite`.
  - Define `FavoriteDao` `interface`.
  - Create `FavoriteDatabase` `singleton class`. 
  - Initialize `dao` `singleton` in `MainActivity` `onCreate()`. 
  - Add `onItemClick` lambda function parameter to the `RestaurantAdapter`.
  - Create `AlertDialog` for user to confirm the selection of item to add to favorite when clicked.
  - Use `lifecycleScope` to insert favorite into the database.
  - Create `FavoriteActivity` with `RecyclerView` similar to the previous one to show the list 
    of Favorites.
  - Add `onItemClick()` to the `RecyclerView` to pop a `AlertDialog`, so user can remove item 
    from the list of favorites.
  
- Create `Navigation Drawer` to guide user between activities:
  - Change the root element of `activity_main.xml` to `androidx.drawerlayout.widget.
    DrawerLayout` and nest all other layouts inside it.
  - Create `com.google.android.material.navigation.NavigationView` at the bottom of other 
    layouts, and set `app:headerLayout=` and `app:menu=` to their files.
  - Create the layout file for the `header` and the resource file for `menu`.
  - Setup `toggle` for the menu in `Activity` and add listener.
  
