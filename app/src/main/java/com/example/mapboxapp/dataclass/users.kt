package com.example.mapboxapp.dataclass

import android.os.Parcel
import android.os.Parcelable

data class users(
    val contribution:String="",
    val solution:String="",
    val problem:String="",
    val imageURL:String="",
    val email:String="",
    val code:String="",
    val name:String="",
    val ischecked:String="",
)
    :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents()=0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(contribution)
        dest.writeString(solution)
        dest.writeString(problem)
        dest.writeString(imageURL)
        dest.writeString(email)
        dest.writeString(code)
        dest.writeString(name)
        dest.writeString(ischecked)
    }

    companion object CREATOR : Parcelable.Creator<users> {
        override fun createFromParcel(parcel: Parcel): users {
            return users(parcel)
        }

        override fun newArray(size: Int): Array<users?> {
            return arrayOfNulls(size)
        }
    }
}

