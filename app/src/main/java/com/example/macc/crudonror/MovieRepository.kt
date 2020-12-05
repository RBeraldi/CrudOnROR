package com.example.macc.crudonror

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class MovieRepository (c: Context?, adapter: MovieAdapter) : MovieCRUD {

    private val queue = Volley.newRequestQueue(c)
   // private var url = "https://shrouded-shore-31030.herokuapp.com/movies"

  //  private var url = "https://pure-beyond-97319.herokuapp.com/movies"

  private var url = "http://192.168.1.74:5000"

    private var adp = adapter

    private var data: MutableMap<Int, Movie> = mutableMapOf()

    //curl -H "Content-Type: application/json" -X POST  -d '{"movie": {"title":"Matrix"}}' https://shrouded-shore-31030.herokuapp.com/movies
    override fun Create(title :String, rate: Int, year: String, description:String): Boolean {

        var req = HashMap<String,String>()
        req["title"]=title
        req["year"]="1/1/"+year
        req["description"]=description
        req["rate"]=rate.toString()

        var movie = Movie()

        movie.title=title
        movie.description=description
        movie.rate=rate
        movie.year=year

        Log.i("ROR.3",req["year"])
        Log.i("info", "Create: "+JSONObject(req.toMap()))

        val stringRequest = JsonObjectRequest(
            Request.Method.POST, url, JSONObject(req.toMap()),
            { r ->
                val key = JSONObject(r.toString()).getInt("id")
                movie.movieID = key
                data.set(key, movie)
                adp.notifyDataSetChanged()
            },
            { error: VolleyError? ->
                Log.e(
                    "ROR",
                    "/get request ERROR!: $error"
                )
            })
        queue.add(stringRequest)
        return true
    }

    //curl -H "Content-Type: application/json" -X GET  https://shrouded-shore-31030.herokuapp.com/movies/movieID
    override fun Read(position: Int): Movie {

        if ((data.isEmpty()) or (data.size < position)) {
            val movie = Movie()
            movie.title = "Error: can't find the movie.. "
        }

        val movieID = data.keys.sorted().get(position)
        Log.i("ROR","Getting movieID "+movieID.toString()+" "+data.get(movieID).toString())
        return data.get(movieID)!!
    }


    //curl -H "Content-Type: application/json" -X GET  https://shrouded-shore-31030.herokuapp.com/movies/
    override fun ReadAll(): Boolean {

        Log.i("ROR","Read all called....")
        if (!data.isEmpty())
            return true

        Log.i("ROR","FETCHING DATA FROM WEB-API....")
        val stringRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { r ->
                val reply = JSONArray(r.toString())
                Log.i("info", "ReadAll: "+reply[0].toString())

                var movieJSON: JSONObject
                var movie: Movie
                for (i in 1..reply.length()-1) {

                    movieJSON = JSONObject(reply[i].toString())
                    movie = Movie()
                    movie.movieID = movieJSON.getInt("id")
                    movie.title = movieJSON.getString("title")
                    movie.year = movieJSON.getString("year")

                    Log.i("ROR.3", movieJSON.getString("year"))

                    movie.description = movieJSON.getString("description")
                    data.put(movie.movieID, movie)

                }
                adp.notifyDataSetChanged()
                Log.i("ROR", data.toString())
            },
            { error: VolleyError? ->
                Log.e(
                    "ROR",
                    "/get request ERROR!: $error"
                )
            })
        // Add the request to the RequestQueue.
        Log.i("ROR", "...sending a request")
        queue.add(stringRequest)
        return true
    }




    override fun Update(position :Int, title :String, rate: Int, year: String, description:String): Boolean {

        var req = HashMap<String,String>()
        req["title"]=title
        req["year"]=year
        req["rate"]=rate.toString()
        req["description"]=description

        val key = data.keys.sorted().get(position)

        Log.i("ROR.2",key.toString())
        //val key = position

        val movie = data.get(key)


        movie!!.title=title
        movie.year=year
        movie.rate=rate
        movie.description=description
        data.set(key,movie)

        adp.notifyItemChanged(position)

        val stringRequest = JsonObjectRequest(
            Request.Method.PUT, url+"/"+key.toString(), JSONObject(req.toMap()),
            { r ->
            },
            { error: VolleyError? ->
                Log.e(
                    "ROR",
                    "/get request ERROR!: $error"
                )
            })
        queue.add(stringRequest)

     return true
    }

    override fun Delete(position: Int): Boolean {
        data.remove(position)
        adp.notifyDataSetChanged()
        val stringRequest = JsonObjectRequest(
            Request.Method.DELETE, url+"/"+position.toString(), null,
            { r ->
            },
            { error: VolleyError? ->
                Log.e(
                    "ROR",
                    "/DELETE ERROR!: $error"
                )
            })
        queue.add(stringRequest)

        return true
    }

    override fun getItemCount(): Int {
       return data.size
    }
}