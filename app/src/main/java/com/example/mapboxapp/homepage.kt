
package com.example.mapboxapp


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Math.abs


class homepage : Fragment() {
    var databaseReference: DatabaseReference? = null
    private lateinit var listLat: ArrayList<Double>
    private lateinit var listCity: ArrayList<String>
    private lateinit var listLog: ArrayList<Double>
    private lateinit var tvOne : TextView
    private lateinit var tvThree : TextView
    private lateinit var tvTwo : TextView
    var eventListener: ValueEventListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_homepage, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val floatingactionbtn=view.findViewById<FloatingActionButton>(R.id.floating_action_home1)
        floatingactionbtn.setOnClickListener {
            val intent= Intent(context,MapActivity::class.java)
            startActivity(intent)
        }



        tvOne = view.findViewById(R.id.text1)
        tvTwo = view.findViewById(R.id.text2)
        tvThree = view.findViewById(R.id.text3)
//        view.findViewById<TextView>(R.id.text1).setText(tvOne.toString())
//        view.findViewById<TextView>(R.id.text2).setText(tvTwo.toString())
//        view.findViewById<TextView>(R.id.text1).setText(tvThree.toString())
//        val Types = arrayOf(
//            "Urban Flooding",
//            "Rural Flooding",
//            "Oil Spill",
//            "Tsunami",
//            "Polluted River",
//            "Drought",
//            "Drainage problems"
//        )
//        listLat = ArrayList()
//        listLog = ArrayList()
//        listCity = ArrayList()
//
//        var cityList = arrayListOf<String>()
//        var typeList = arrayListOf<String>()
//        for (i in 0..6) {
//            //  var j: Int?= null
//            databaseReference = FirebaseDatabase.getInstance().reference
//
//            val reference = databaseReference!!.child("problems").child(Types[i])
//
//            eventListener = reference!!.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    listLat.clear()
//                    listLog.clear()
//                    listCity.clear()
//                    if (snapshot.exists()) {
//                        for (itemSnapshot in snapshot.children) {
//
//                            listLat.add(itemSnapshot.child("locationLat").value as Double)
//                            listLog.add(itemSnapshot.child("locationLong").value as Double)
//                            listCity.add(itemSnapshot.child("city").value.toString())
//                        }
//                        val size = listLat.size
//
//                        //   var f: Boolean=false
//                        for (k in 0..(size - 1)) {
//                            var cnt = 0
//                            for (j in (k + 1)..(size - 1)) {
//                                if (abs(listLat[k] - listLat[j]) <= 1.5 && abs(listLog[k] - listLog[j]) <= 1.5) cnt++
//                            }
//                            if (cnt >= 7) {
//                                cityList.add(listCity[k])
//                                typeList.add(Types[i])
//
//                            }
//                        }
//
//
//                    }
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//
//                }
//            })
//            tvOne.setText("${typeList[0]} is predicted in ${cityList[0]}")
//            tvTwo.setText("${typeList[1]} is predicted in ${cityList[1]}")
//            tvThree.setText("${typeList[2]} is predicted in ${cityList[2]}")


//        }

    }
}