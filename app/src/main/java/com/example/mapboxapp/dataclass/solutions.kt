package com.example.mapboxapp.dataclass

data class solutions(
    //val imageURL: String,
    val providedBy: String="",
    val problem:String="",
    val description:String="",
    val type:String="",
    val supporters: ArrayList<String> = arrayListOf<String>(),
    var upvoteCount: Long=0,
    val city: String=""
)
