package com.example.mapboxapp

import com.example.mapboxapp.Adapter.ReachOutAdapter
import com.example.mapboxapp.Models.ReachOutViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapboxapp.dataclass.problems


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReachOut.newInstance] factory method to
 * create an instance of this fragment.
 */

private lateinit var viewModel: ReachOutViewModel
private lateinit var itemRecyclerView: RecyclerView
lateinit var adapter: ReachOutAdapter
var items = ArrayList<problems>()

class ReachOut : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reach_out2, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReachOut.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReachOut().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemRecyclerView = view.findViewById(R.id.recyclerView)
        itemRecyclerView.layoutManager = LinearLayoutManager(context)
        itemRecyclerView.setHasFixedSize(true)
        adapter = ReachOutAdapter(requireContext(), items,this)
        itemRecyclerView.adapter= adapter
        createDummyItemList()

        viewModel = ViewModelProvider(this).get(ReachOutViewModel::class.java)
        viewModel.allItems.observe(viewLifecycleOwner, Observer {
            adapter.updateItemList(it)
        })
    }

    private fun createDummyItemList(){
        for (i in 1.. 100){
            //items.add((problems("Item"+i)))
        }
    }

    fun onItemItemClicked(position: Int){
//        val intent = Intent(this@ReachOut.requireContext(),TutorDes::class.java)
//        intent.putExtra("firstname", items[position].firstName)
//        intent.putExtra("lastname",items[position].lastName)
//        intent.putExtra("image", items[position].image)
//        intent.putExtra("monthlyfee",items[position].monthlyfee)
//        intent.putExtra("doubtfee", items[position].doubtfee)
//        intent.putExtra("qualifications", items[position].qualifications)
//        intent.putExtra("dayandtime", items[position].dayandtime)
//        intent.putExtra("state", items[position].state)
//        intent.putExtra("adress", items[position].adress)
//        intent.putExtra("averagerating", items[position].averagerating)
//        intent.putExtra("hiringstatus", items[position].hiringstatus)
//        intent.putExtra("students", items[position].studentssubscribed)
//        intent.putExtra("email",items[position].email)
//        intent.putExtra("ratings",items[position].averagerating)
//        intent.putExtra("uid",items[position].uid)
        //startActivity(intent)
    }
}