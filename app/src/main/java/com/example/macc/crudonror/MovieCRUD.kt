package com.example.macc.crudonror


interface MovieCRUD {

    fun Create(title: String, rate: Int, year: String, description:String): Boolean
    fun ReadAll() : Boolean
    fun Read(position: Int) : Movie
    fun Update(moveid: Int, title: String, rate: Int, year: String, description:String): Boolean
    fun Delete(position: Int) : Boolean
    fun getItemCount(): Int

}