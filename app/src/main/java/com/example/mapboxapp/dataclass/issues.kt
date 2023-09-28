package com.example.mapboxapp.dataclass

data class issues(
    val imageURL:String="",
    val description:String="",
    val estimatedloss:String="",
    val title: String="",
    val locationLat: Double=0.0,
    val locationLong: Double=0.0,
    val city: String="",
    val username: String="",
    val type:String="none"
)
