package com.example.informationnation

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var mainSearchBarList: MutableList<Feature> = ArrayList()
    var locationsList: MutableList<Feature> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView_main.layoutManager = LinearLayoutManager(this)

        recyclerView_main.adapter = MainAdapter(homeFeed = mainSearchBarList)

        fetchJson()

        handleSearch()
    }

    private fun handleSearch() {

        val searchBar = searchView_listSearch
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return searchBar.isIconified
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    mainSearchBarList.clear()
                    val keyword = newText.toLowerCase(Locale.ROOT)
                    locationsList.forEach {
                        if (it.properties.name?.toLowerCase(Locale.ROOT)
                                ?.contains(keyword)!!
                        ) {
                            mainSearchBarList.add(it)
                        }
                    }
                    recyclerView_main.adapter?.notifyDataSetChanged()
                } else {
                    mainSearchBarList.clear()
                    mainSearchBarList.addAll(locationsList)
                    recyclerView_main.adapter?.notifyDataSetChanged()
                }
                return true
            }
        })
    }

    fun fetchJson(){
        println("Attempting to fetch JSON")

        val url = "https://www.noforeignland.com/home/api/v1/places/"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()

                locationsList = gson.fromJson(body, HomeFeed::class.java).features

                mainSearchBarList.addAll(locationsList)

                runOnUiThread { recyclerView_main.adapter = MainAdapter(homeFeed = mainSearchBarList) }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request!")
            }
        })
    }
}







