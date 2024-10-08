package com.example.flixster


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private var movieList: MutableList<Movie> = mutableListOf()

    private val API_URL = "https://api.themoviedb.org/3/movie/now_playing"
    private val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed" // Replace with your TMDB API key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        movieAdapter = MovieAdapter(movieList, this)
        recyclerView.adapter = movieAdapter

        fetchMovies()
    }

    private fun fetchMovies() {
        val client = AsyncHttpClient()
        val url = "$API_URL?api_key=$API_KEY&language=en-US&page=1"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                response?.let {
                    val results: JSONArray = it.getJSONArray("results")
                    for (i in 0 until results.length()) {
                        val movieObject = results.getJSONObject(i)
                        val title = movieObject.getString("title")
                        val description = movieObject.getString("overview")
                        val posterPath = movieObject.getString("poster_path")

                        val movie = Movie(title, description, posterPath)
                        movieList.add(movie)
                    }
                    movieAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable?,
                errorResponse: JSONObject?
            ) {
                Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
