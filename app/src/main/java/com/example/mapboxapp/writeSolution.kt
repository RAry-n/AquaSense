package com.example.mapboxapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mapboxapp.dataclass.solutions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class writeSolution : AppCompatActivity() {
    private lateinit var pd : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_solution)
        val items = listOf(
            "none",
            "Urban Flooding",
            "Rural Flooding",
            "Oil Spill",
            "Tsunami",
            "Polluted River",
            "Drought",
            "Drainage problems"
        )
        val autoComplete: AutoCompleteTextView = findViewById(R.id.type)
        val adapter = ArrayAdapter(this, R.layout.item_list, items)

        autoComplete.setAdapter(adapter)

        var type: String? = null

        autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val itemSelected = parent.getItemAtPosition(position)
                type = itemSelected.toString()
            }



        val button: Button = findViewById(R.id.button)

        button.setOnClickListener {
            pd = ProgressDialog(this)
            pd.setMessage("uploading...")
            pd.show()
            val title1 = findViewById<EditText>(R.id.title).text.toString()
            val location = findViewById<EditText>(R.id.location).text.toString()
            val description = findViewById<EditText>(R.id.description).text.toString()
            val uid = FirebaseAuth.getInstance().currentUser!!.uid

            if (title == null || location == null || type == null || description == null || uid == null) {
                pd.dismiss()
                Toast.makeText(this, "Insufficient Information", Toast.LENGTH_SHORT).show()

            } else {
                val data = solutions(
                    type = type!!,
                    providedBy = uid,
                    problem = title1,
                    description = description,
                    supporters = arrayListOf(),
                    upvoteCount = 0,
                    city = location
                )

                FirebaseDatabase.getInstance().getReference("solutions").child(type!!).push()
                    .setValue(data).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Thanks for contributing", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        pd.dismiss()
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Something won't wrong", Toast.LENGTH_SHORT).show()
                        pd.dismiss()
                    }
                }

            }
        }
    }
}