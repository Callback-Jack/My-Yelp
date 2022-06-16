# My Yelp 

These are the key steps for this project:
  
- Project setup:
  - Add dependencies for `Retrofit`, `Coroutines` and `Coroutine Lifecycle Scopes`
  - Enable view binding
  - setup user permission for Internet
  
- Build the layout:
  - For the main activity, we need `SearchView`, `Spinner` and `RecyclerView`
  - For the recycler item layout, we use `LinearLayout` to arrange the attributes
  
- Connect to Yelp API:
  - First we need to register a project to get the api key
  - We create the API `interface` using `Retrofit`
  - We create a `Retrofit` instance singleton
  - We customize the `RecyclerView` adapter using `DiffUtil`
  - We create the data class by parsing the API's `JSON` response using the plugin for conversion
  
- Display the result:
  - setup the binding in `MainActivity`
  - setup the `RecyclerView` adapter
  - use `lifecycleScope` to retrieve data from the API 
  - catch the `IOException` and `HttpException`
  
- Create the Search bar for user to search restaurants:
  - Create the view using `SearchView`
  - Set the `setOnQueryTextListener` in the `SearchView`
  - Override the functions: `onQueryTextSubmit(query: String?)` and `onQueryTextChange(newText: String?)` 
  
- Create some sorting methods for the restaurant list:
  - Extend the `MainActivity` with `AdapterView.OnItemSelectedListener`
  - Setup the `spinner.adapter` with `R.array.spinner_options` which has the options to display
  - Override the `onItemSelected()` with the callback functions implemented with the sorting methods
  
