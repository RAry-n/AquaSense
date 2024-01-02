package com.example.mapboxapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.mapboxapp.Adapter.FragmentBodyAdapter
import com.google.firebase.auth.FirebaseAuth
import com.ismaeldivita.chipnavigation.ChipNavigationBar

private lateinit var navBar:ChipNavigationBar
private lateinit var viewPager : ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        firebaseAuth = FirebaseAuth.getInstance()

        navBar = findViewById(R.id.navBar)
        navBar.setMenuResource(R.menu.navmenu)
        //tabLayout : TabLayout= tabLayout
        //viewPager: ViewPager = viewPager
        viewPager = findViewById(R.id.viewPager)


        val adapter = FragmentBodyAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        viewPager.currentItem=0
        navBar.setItemSelected(0)
//        val homeFragment = HomeFragment()
//        val issues = IssueFragment()
//        val solutins = ReachOut()
//        val settings = settings()
        viewPager.isUserInputEnabled = false
        navBar.setOnItemSelectedListener { id ->
            // 0 -> viewPager.currentItem =
            when (id) {
                R.id.home -> viewPager.currentItem = 0
                R.id.issues -> viewPager.currentItem = 1
                R.id.solutions -> viewPager.currentItem = 2
                R.id.settings -> viewPager.currentItem = 3
                else -> viewPager.currentItem = 2
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu!!.add("Log Out")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.title?.equals("Log Out") == true){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("LOG OUT")
            builder.setMessage("Are you sure?")
            builder.setPositiveButton("LOG OUT", DialogInterface.OnClickListener{ dialog, which->
                firebaseAuth.signOut()
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
        if(item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}

