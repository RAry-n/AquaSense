package com.example.mapboxapp


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapboxapp.Adapter.issuesAdapter
import com.example.mapboxapp.dataclass.issues
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class IssueFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var itemRecyclerView: RecyclerView

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

    private val issueList = ArrayList<issues>()

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
        return inflater.inflate(R.layout.fragment_issue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val floatingactionbtn=view.findViewById<FloatingActionButton>(R.id.floating_action_home)
        floatingactionbtn.setOnClickListener {
            val intent= Intent(context,ReportProblem::class.java)
            startActivity(intent)
        }

        itemRecyclerView= view.findViewById(R.id.recyclerViewImg)
        itemRecyclerView.layoutManager = LinearLayoutManager(context)
        itemRecyclerView.setHasFixedSize(true)

        dbref = FirebaseDatabase.getInstance().getReference("problems")

        fetchRecyclerView()

    }

    private fun fetchRecyclerView() {
        issueList.clear()
        for(i in 1..7){
            dbref.child(problemTypes[i]).addValueEventListener( object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(dataSnap in snapshot.children){
                            val data = dataSnap?.getValue(issues::class.java)
                            if(data!=null)
                            {
                                issueList.add(data)
                            }

                        }

                        val itemAdapter = issuesAdapter(issueList)
                        itemRecyclerView.adapter = itemAdapter

//                        itemAdapter.setOnItemClickListener(object: issuesAdapter.onItemClickListener{
//                            override fun onItemClick(position: Int) {
//                                //onClick
//
//                            }
//
//                        })

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show()
                }
            })

        }
    }

}