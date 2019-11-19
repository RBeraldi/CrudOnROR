package com.example.macc.crudonror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.movie_layout.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        list.addItemDecoration(itemDecoration)

        val adapter = MovieAdapter(this)

        list.layoutManager= LinearLayoutManager(this)
        list.adapter=adapter
        fab.setOnClickListener(adapter)
        /*
        { view ->
            val builder = AlertDialog.Builder(this)
            val dialogView = LayoutInflater.from(this).inflate(R.layout.movie_createupdate,null)
            builder.setTitle("Create a new movie")
            builder.setView(dialogView)
            // add a button
            builder.setPositiveButton("OK", null);
            builder.create().show()
        }

         */
    }



}
