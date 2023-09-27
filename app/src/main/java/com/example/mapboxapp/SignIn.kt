package com.example.mapboxapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mapboxapp.dataclass.users
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignIn : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var mdatabaseref: DatabaseReference
    private lateinit var dialogBox:Dialog
    private lateinit var     type:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        auth= FirebaseAuth.getInstance()
        val intent= Intent(this, SignUpautho::class.java)
        val signup=findViewById<TextView>(R.id.txtNewHereSign)
        signup.setOnClickListener {
            startActivity(intent)
        }
        mdatabaseref= FirebaseDatabase.getInstance().getReference("Users")
        findViewById<Button>(R.id.btnSignin).setOnClickListener{
            signinregistereduser()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    private fun signinregistereduser()
    {
        val email:String=findViewById<EditText>(R.id.EmailEntryField).text.toString().trim{it<=' '}
        val password:String=findViewById<EditText>(R.id.EmailEntryField1).text.toString().trim{it<=' '}
        if (revalidate1(email,password)) {
            showProgressDialog("Signing In...")
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) { task->
                hideProgressDialog()
                if(task.isSuccessful){
                  val firebaseuser:FirebaseUser=task.result!!.user!!
                    val uid1=firebaseuser.uid

                    val registeredemail=firebaseuser.email!!
                    val layout123=layoutInflater.inflate(R.layout.custom_toast_layout,findViewById(R.id.view_layout_of_toast))
                    val   toast4: Toast = Toast(this)
                    toast4.view=layout123
                    val txtmsg12:TextView=layout123.findViewById(R.id.textview_toast)
                    txtmsg12.setText( "You have Signed In successfully")
                    toast4.duration.toLong()
                    toast4.show()

//                    finish()

                 mdatabaseref.child(auth.currentUser!!.uid).addValueEventListener(object:ValueEventListener
                  {
                      override fun onDataChange(snapshot: DataSnapshot) {
                              var user1=snapshot.getValue(users::class.java)
                               type=user1?.ischecked.toString()
                          if(type=="org")
                          {
                              val intent1=Intent(this@SignIn,MainActivity::class.java)
                              startActivity(intent1)
                          }
                          else
                          {
                              val intent2=Intent(this@SignIn, MainActivity2::class.java)
                              startActivity(intent2)
                          }

                      }

                      override fun onCancelled(error: DatabaseError) {
                          TODO("Not yet implemented")
                      }

                  }

                  )


                    Log.d("Sign IN","Sign with Email successfully")





                }
                else{
                    //if sign in fails display the message to the users

                    val layout1=layoutInflater.inflate(R.layout.error_toast_layout,findViewById(R.id.view_layout_of_toast1))
                    val toast1: Toast = Toast(this)
                    toast1.view=layout1
                    val txtmsg1:TextView=layout1.findViewById(R.id.textview_toast1)
                    txtmsg1.setText("Authentication failed,Please enter correct credentials.")
                    toast1.duration.toShort()
                    toast1.show()
                    Log.w("Sign In","sign with email failed",task.exception)

                }


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
    fun showerrorsnackbar(message:String){
        // gives the root element of a view without actually knowing its id
        val snackbar =Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackbarview=snackbar.view
        snackbarview.setBackgroundColor(ContextCompat.getColor(this, com.jpardogo.android.googleprogressbar.library.R.color.red))
        snackbar.show()

    }
    fun hideProgressDialog()
    {
        dialogBox.dismiss()
    }

    private fun revalidate1(
        email1: String,
        password1: String
    )
            : Boolean {
        return when{
            TextUtils.isEmpty(email1)->{
                showerrorsnackbar("Please enter your Email Address")
                false
            }
            TextUtils.isEmpty(password1)->{
                showerrorsnackbar("Please enter your Password")
                false
            }
            else->{
                true
            }

        }
    }

}