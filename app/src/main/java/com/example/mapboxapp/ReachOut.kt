package com.example.mapboxapp

import android.graphics.Color.parseColor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapboxapp.Adapter.ReachOutAdapter
import com.example.mapboxapp.dataclass.problems
import com.example.mapboxapp.dataclass.solutions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private lateinit var dbref : DatabaseReference
private lateinit var dbref1 : DatabaseReference
private lateinit var itemRecyclerView: RecyclerView
private lateinit var firebaseAuth: FirebaseAuth

var items = ArrayList<problems>()

val isChecked = arrayListOf<Boolean>(
    true,false,false,false,false,false,false,false
)

val problemTypes = arrayOf(
    "none",
    "Urban Flooding",
    "Rural Flooding",
    "Oil Spill",
    "Tsunami",
    "Polluted River",
    "Drought",
    "Drainage problems"
)

private val solutionList = ArrayList<solutions>()

class ReachOut : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reach_out2, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemRecyclerView = view.findViewById(R.id.recyclerView)
        itemRecyclerView.layoutManager = LinearLayoutManager(context)
        itemRecyclerView.setHasFixedSize(true)

        dbref = FirebaseDatabase.getInstance().getReference("solutions")

        fetchRecyclerView()
        callUser(view)

    }

    private fun callUser(view: View) {
        val btn0: Button = view.findViewById(R.id.btn0)
        val btn1: Button = view.findViewById(R.id.btn1)
        val btn2: Button = view.findViewById(R.id.btn2)
        val btn3: Button = view.findViewById(R.id.btn3)
        val btn4: Button = view.findViewById(R.id.btn4)
        val btn5: Button = view.findViewById(R.id.btn5)
        val btn6: Button = view.findViewById(R.id.btn6)
        val btn7: Button = view.findViewById(R.id.btn7)

        val btn = arrayListOf<Button>(btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7)

        for(i in 0..7){
            btn[i].setOnClickListener {
                if(isChecked[i]){
                    isChecked[i]=false
                    btn[i].setTextColor(parseColor("#000000"))
                    btn[i].backgroundTintList= this.getResources().getColorStateList(R.color.tint)
                }else{
                    isChecked[i]=true
                    btn[i].setTextColor(parseColor("#FFFFFF"))
                    btn[i].backgroundTintList= this.getResources().getColorStateList(R.color.black)
                    if(i==0){
                        for(j in 1..7){
                            isChecked[j]=false
                            btn[j].setTextColor(parseColor("#000000"))
                            btn[j].backgroundTintList= this.getResources().getColorStateList(R.color.tint)
                        }
                    } else{
                        isChecked[0]=false
                        btn[0].setTextColor(parseColor("#000000"))
                        btn[0].backgroundTintList= this.getResources().getColorStateList(R.color.tint)
                    }
                }
                fetchRecyclerView()
            }
        }
    }

    private fun fetchRecyclerView() {
        solutionList.clear()
        for(i in 1..7){
            if(isChecked[i] or isChecked[0]){
                dbref.child(problemTypes[i]).addValueEventListener( object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for(dataSnap in snapshot.children){
                                val data = dataSnap.getValue(solutions::class.java)
                                solutionList.add(data!!)
                            }

                            val itemAdapter = ReachOutAdapter(solutionList)
                            itemRecyclerView.adapter = itemAdapter

                            itemAdapter.setOnItemClickListener(object: ReachOutAdapter.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    //onClick

                                }

                            })

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }


}