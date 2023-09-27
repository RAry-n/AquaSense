package com.example.mapboxapp

import androidx.lifecycle.MutableLiveData
import com.example.mapboxapp.dataclass.problems
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class IssueRepository {

    val Types= arrayOf("Urban Flooding","Rural Flooding","Oil Spill","Tsunami","Polluted River","Drought","Drainage problems")

        private val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("problems")


        @Volatile
        private var INSTANCE: IssueRepository? = null

        fun getInstance(): IssueRepository {
            return INSTANCE ?: synchronized(this) {

                val instance = IssueRepository()
                INSTANCE = instance
                instance
            }
        }

        fun loadItems(itemList: MutableLiveData<List<problems>>) {


            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try{
                        val typeList: List<String> = snapshot.children.map { dataSnapshot ->
                            dataSnapshot.getValue().toString()
                        }
                    }catch (e: java.lang.Exception){

                    }
//                    try {
//                        val _itemList: List<problems> = snapshot.children.map { dataSnapshot ->
//                            dataSnapshot.getValue(problems::class.java)!!
//                        }
//                        itemList.postValue(_itemList)
//                    } catch (e: java.lang.Exception) {
//
//                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            fun main(args : Array<String>) {
                args.asList().filter { it -> it.length > 0 }.forEach { println("Hello, $it!") }
            }

//            databaseReference.
       }
    }
