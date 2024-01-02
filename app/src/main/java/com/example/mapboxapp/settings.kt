package com.example.mapboxapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mapboxapp.dataclass.users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [settings.newInstance] factory method to
 * create an instance of this fragment.
 */
class settings : Fragment() {

    private lateinit var mauth: FirebaseAuth
    private lateinit var mdatabaseref: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val imageview = view.findViewById<CircleImageView>(R.id.imageView3)
        val username = view.findViewById<TextView>(R.id.textView2)
        mauth = FirebaseAuth.getInstance()
        mdatabaseref = FirebaseDatabase.getInstance().getReference("user")
        mdatabaseref.child(mauth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user1 = snapshot?.getValue(users::class.java)
                    Glide.with(requireContext())
                        .load(user1?.imageURL)
                        .centerCrop()
                        .placeholder(R.drawable.editprofileme)
                        .into(imageview)
                    username.setText(user1?.name.toString())

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
//        val view1 = view.findViewById<LinearLayout>(R.id.viewofedit)
//        view1.setOnClickListener {
//            val intent1 = Intent(requireContext(), editprofile::class.java)
//            startActivity(intent1)
//        }
//        val view2 = view.findViewById<LinearLayout>(R.id.layoutofaboutus)
//        view2.setOnClickListener {
//            val intent2 = Intent(requireContext(), aboutus::class.java)
//            startActivity(intent2)
//        }
//        val view3 = view.findViewById<LinearLayout>(R.id.layoutofprivacypolicy)
//        view3.setOnClickListener {
//            val intent3 = Intent(requireContext(), privacypolicy::class.java)
//            startActivity(intent3)
//        }
//        val view4 = view.findViewById<LinearLayout>(R.id.layoutoftermsandcondition)
//        view4.setOnClickListener {
//            val intent4 = Intent(requireContext(), termscondition::class.java)
//            startActivity(intent4)
//        }




        return view
    }


}