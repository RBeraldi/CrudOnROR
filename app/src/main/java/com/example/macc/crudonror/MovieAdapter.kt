package com.example.macc.crudonror

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.movie_layout.view.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.movie_createupdate.view.*
import kotlinx.android.synthetic.main.movie_layout.view.movieItem


class MovieAdapter (c: Context?) : RecyclerView.Adapter<MovieAdapter.ViewHolder> (), View.OnClickListener, View.OnLongClickListener
{

    private var repository: MovieRepository= MovieRepository(c,this)
    private var c :Context? = c
    init {
        Log.i("ROR","init called"+repository.toString())
        repository.ReadAll()
    }

    override fun onLongClick(v: View): Boolean {
        repository.Delete(v.movieID.text.toString().toInt())
        return true
    }


    override fun onClick(it: View) {
            val builder = AlertDialog.Builder(c!!)
            val dialogView = LayoutInflater.from(c!!).inflate(R.layout.movie_createupdate,null)
            if (it.id==R.id.fab) {
                builder.setTitle("Create a new Movie ")
                builder.setView(dialogView)
                dialogView.edit_title.hint="Movie title"
                dialogView.edit_year.hint="year"
                dialogView.edit_description.hint="description"
                builder.setPositiveButton("Create", DialogInterface.OnClickListener { dialog, which ->
                    repository.Create(
                        title=dialogView.edit_title.text.toString(),
                        rate=5,
                        year=dialogView.edit_year.text.toString(),
                        description=dialogView.edit_description.text.toString())
                }
                )
            }
            else {
                builder.setTitle("Update " + it.title.text)
                builder.setView(dialogView)
                dialogView.edit_title.text.insert(0, it.title.text)
                dialogView.edit_year.text.insert(0, it.year.text)
                dialogView.edit_description.text.insert(0, it.description.text)
                val pos = it.position.text.toString().toInt()
                builder.setPositiveButton(
                    "Update",
                    DialogInterface.OnClickListener { dialog, which ->
                        repository.Update(
                            position = pos,
                            title = dialogView.edit_title.text.toString(),
                            rate = 5,
                            year = dialogView.edit_year.text.toString(),
                            description = dialogView.edit_description.text.toString()
                        )

                    }
                )
            }
            builder.setNegativeButton("Cancel", null);
            builder.create().show()

        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

       // return 10
       return repository.getItemCount()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie : Movie= repository.Read(position)
        holder.itemView.videoView.setVideoPath("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
        holder.itemView.title.text=movie.title
        holder.itemView.year.text= movie.year.toString()
        holder.itemView.description.text=movie.description
        holder.itemView.movieID.text=movie.movieID.toString()
        holder.itemView.position.text=position.toString()

        holder.itemView.videoView.setOnClickListener {
            if (it.videoView.isPlaying) {
                it.videoView.pause()
            }
            else
                 it.videoView.start()
        }

        holder.itemView.movieItem.setOnClickListener(this)
        holder.itemView.movieItem.setOnLongClickListener(this)

        /*

        {
            val builder = AlertDialog.Builder(c!!)
            val dialogView = LayoutInflater.from(c!!).inflate(R.layout.movie_createupdate,null)
            builder.setTitle("Update "+it.title.text)
            builder.setView(dialogView)
            dialogView.edit_title.text.insert(0,it.title.text)
            dialogView.edit_year.text.insert(0,it.year.text)
            dialogView.edit_description.text.insert(0,it.description.text)
            builder.setPositiveButton("Update", DialogInterface.OnClickListener { dialog, which ->
                repository.Update(
                    position=position,
                    title=dialogView.edit_title.text.toString(),
                    rate=5,
                    year=dialogView.edit_year.text.toString().toInt(),
                    description=dialogView.edit_description.text.toString())

                }
            )
            builder.setNegativeButton("Cancel", null);
            builder.create().show()
        }

         */
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}





}






//holder.itemView.setOnClickListener { Toast.makeText(c,"ok",Toast.LENGTH_SHORT).show() }
