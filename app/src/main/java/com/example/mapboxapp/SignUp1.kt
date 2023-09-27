package com.example.mapboxapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mapboxapp.dataclass.users
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp1 : AppCompatActivity() {
    private lateinit var mdatabaseref: DatabaseReference
    private lateinit var dialogBox:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up1)
        val btnauth=findViewById<RadioButton>(R.id.rbAuthority)
        val btnresident=findViewById<RadioButton>(R.id.rbResident)
        btnauth.setOnClickListener {
            val intent1=Intent(this, SignUpautho::class.java)
            startActivity(intent1)
            btnresident.isChecked=false
        }
       mdatabaseref= FirebaseDatabase.getInstance().getReference("Users")
        val btnsignup=findViewById<Button>(R.id.btnresident)
        btnsignup.setOnClickListener {
            registerUser()
        }
    }

    override fun onBackPressed() {

        super.onBackPressed()
        finishAffinity()
        val intent1=Intent(this, SignIn::class.java)
        startActivity(intent1)
    }
    private fun registerUser()
    {

        val name:String=findViewById<EditText>(R.id.nameEntryField).text.toString().trim{it<=' '}
        val email:String=findViewById<EditText>(R.id.EmailEntryField).text.toString().trim{it<=' '}
        val password:String=findViewById<EditText>(R.id.passwordEntryField).text.toString().trim{it<=' '}

        if(validateForm(name,email,password))
        {
            showProgressDialog("Signing Up...")
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password
            ).addOnCompleteListener {
                    task->
                hideProgressDialog()
                if(task.isSuccessful)
                {
                    val firebaseuser: FirebaseUser =task.result!!.user!!
                    val firebaseemail=firebaseuser.email
                    val layout=layoutInflater.inflate(R.layout.custom_toast_layout,findViewById(R.id.view_layout_of_toast))
                    val toast= Toast(this)
                    toast.view=layout
                    val txtmsg=layout.findViewById<TextView>(R.id.textview_toast)
                    txtmsg.setText("You have successfully registered")
                    toast.duration.toLong()
                    toast.show()

                    val user= users(name = name, email = email, ischecked = "resi")
                    mdatabaseref.child("${firebaseuser.uid}").setValue(user).addOnCompleteListener {

                        finishAffinity()
                        val intent:Intent=Intent(this, MainActivity2::class.java)
                        startActivity(intent)
                    }








                }
                else
                { val layout1=layoutInflater.inflate(R.layout.error_toast_layout,findViewById(R.id.view_layout_of_toast1))
                    val toast= Toast(this)
                    toast.view=layout1
                    val txtMsg=layout1.findViewById<TextView>(R.id.textview_toast1)
                    txtMsg.setText("${task.exception!!.message}")
                    toast.duration.toLong()
                    toast.show()


                }

            }

        }



    }
    private fun validateForm(name:String,email:String,password:String):Boolean
    {
        return when{
            TextUtils.isEmpty(name)->
            {
                errorSnackBar("Please Enter your Name")
                false
            }
            TextUtils.isEmpty(email)->
            {
                errorSnackBar("Please Enter your Email")
                false
            }
            TextUtils.isEmpty(password)->
            {
                errorSnackBar("Please Enter your Password")
                false
            }

            else->
            {
                true
            }
        }

    }
    fun showProgressDialog(text:String)
    {
        dialogBox= Dialog(this)
        dialogBox.setContentView(R.layout.dialog_progress)
        dialogBox.findViewById<TextView>(R.id.progress_bar_text).text=text
        dialogBox.show()

    }
    fun errorSnackBar(message:String)
    {
        val snackbar= Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG)
        val snackBarView=snackbar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, com.jpardogo.android.googleprogressbar.library.R.color.red))
        snackbar.show()

    }
    fun hideProgressDialog()
    {
        dialogBox.dismiss()
    }

}