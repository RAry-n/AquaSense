package com.example.mapboxapp

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.Window
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.mapboxapp.dataclass.users
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class editprofile : AppCompatActivity() {

    private lateinit var  mprogressdialog: Dialog
    private lateinit var mdatabaseref: DatabaseReference
    private lateinit var profileimage:ImageView
    private lateinit var profilename:EditText
    private lateinit var profileno:EditText
    private lateinit var profileemail:EditText
    private lateinit var profileadress:EditText
    private lateinit var profilebutton:Button
    private var mselectedimageuri: Uri?=null
    private lateinit var mauth: FirebaseAuth
    private lateinit var muserdetails: users
    private var mprofileimageuri:String=""
    val galleryrequestcode:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)
      mdatabaseref=FirebaseDatabase.getInstance().getReference("user")

     profileimage=findViewById(R.id.imageView)
        profilename=findViewById(R.id.nameEntryField)
        profileno=findViewById(R.id.phoneEntryField)
        profileemail=findViewById(R.id.emailEntryField)
        profileadress=findViewById(R.id.addressEntryField)
        profilebutton=findViewById(R.id.btnedit)
        profileimage.setOnClickListener {
           chooseimage()
        }
        mauth=FirebaseAuth.getInstance()

        profilebutton.setOnClickListener{
            if(mselectedimageuri!=null)
            {
                uploaduserimage()
            }
            else{
                showprogressdialog("Please Wait...")
                updateuserprofiledata()
            }

        }
        mauth=FirebaseAuth.getInstance()
        val currentuserid=mauth.uid.toString()
        mdatabaseref.child(currentuserid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user1=snapshot.getValue(users::class.java)

                profilename.setText(user1?.name.toString())
                profileemail.setText(user1?.email.toString())
                profileno.setText(user1?.phone.toString())
                 profileadress.setText(user1?.address.toString())




                Glide.with(applicationContext)
                    .load(user1?.imageURL)
                    .centerCrop()
                    .placeholder(R.drawable.editprofileme)
                    .into(profileimage)
                if (user1 != null) {
                    muserdetails=user1
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"Sorry can't be able to fetch data please check your internet connectivity",
                    Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun uploaduserimage() {
        showprogressdialog("Please Wait...")
        if(mselectedimageuri!=null)
        {
            val sref: StorageReference = FirebaseStorage.getInstance().
            getReference("Users_profile_images").
            child("User_image"+System.currentTimeMillis()+"."+getfileextension(mselectedimageuri))
            sref.putFile(mselectedimageuri!!).addOnSuccessListener {
                    tasksnapshot->
                tasksnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    //this is actual link we want where data stored in firebase
                        uri->

                    mprofileimageuri = uri.toString()

                    updateuserprofiledata()


                }
                hideprogressdialog()
            }.addOnFailureListener{

                Toast.makeText(this@editprofile,it.message,Toast.LENGTH_SHORT).show()
                hideprogressdialog()
            }


        }
    }
    private fun getfileextension(uri:Uri?):String?
    {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))

    }

    private fun updateuserprofiledata() {
        var anychangesmade=false
        val userhashmap=HashMap<String,Any>()
        if(mprofileimageuri.isNotEmpty() && mprofileimageuri!=muserdetails.imageURL)
        {
            userhashmap["imageURL"]=mprofileimageuri
            anychangesmade=true
        }
        if(profilename.text.toString()!=muserdetails.name)
        {
            userhashmap["name"]=profilename.text.toString()
            anychangesmade=true
        }
        if(profileemail.text.toString()!=muserdetails.email)
        {
            userhashmap["email"]=profileemail.text.toString()
            anychangesmade=true
        }
        if(profileno.text.toString()!=muserdetails.phone)
        {
            userhashmap["phone"]=profileno.text.toString()
            anychangesmade=true
        }
        if(profileadress.text.toString()!=muserdetails.address)
        {
            userhashmap["address"]=profileadress.text.toString()
        }
        if(anychangesmade)
        {
            updateuserprofiledata1(userhashmap)
        }
        if(!anychangesmade)
        {
            val layout1 =layoutInflater.inflate(
                R.layout.error_toast_layout,findViewById(
                    R.id.view_layout_of_toast1))
            val toast1: Toast=Toast(this)
            toast1.view=layout1
            val txtmsg:TextView=layout1.findViewById(R.id.textview_toast1)
            txtmsg.setText("You have not updated any data")
            toast1.duration.toShort()
            toast1.show()
            hideprogressdialog()
        }
    }



    private fun chooseimage() {
        val intent1= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent1,galleryrequestcode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK && requestCode==galleryrequestcode && data!!.data!=null)
        {
            mselectedimageuri=data.data

            Glide.with(this@editprofile)
                .load(mselectedimageuri)
                .centerCrop()
                .placeholder(R.drawable.editprofileme)
                .into(profileimage)

        }
    }

    fun showprogressdialog(Text:String){
        mprogressdialog= Dialog(this)
        mprogressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mprogressdialog.setContentView(R.layout.dialog_progress)
        mprogressdialog.setCancelable(false)
        mprogressdialog.findViewById<TextView>(R.id.progress_bar_text).text=Text

        mprogressdialog.show()
        Handler().postDelayed({},2000)


    }
    fun hideprogressdialog()
    {
        mprogressdialog.dismiss()
    }
    private fun updateuserprofiledata1(userhashmap1:HashMap<String,Any>)
    {
        val currentuserid=mauth.uid.toString()
        mdatabaseref.child(currentuserid).updateChildren(userhashmap1).addOnSuccessListener {
            val layout  = layoutInflater.inflate(
                R.layout.custom_toast_layout,findViewById(
                    R.id.view_layout_of_toast))
            val toast:Toast= Toast(this)
            toast.view=layout
            val  txtmst:TextView=layout.findViewById(R.id.textview_toast)
            txtmst.text ="Your Profile Updated Successfully"
            toast.duration.toLong()
            toast.show()
            hideprogressdialog()
            finish()
        }.addOnFailureListener {
            hideprogressdialog()
            Toast.makeText(this,"Profile update error",Toast.LENGTH_SHORT).show()
        }
    }

    fun showerrorsnackbar(message:String){
        // gives the root element of a view without actually knowing its id
        val snackbar = Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG)
        val snackbarview=snackbar.view
        snackbarview.setBackgroundColor(ContextCompat.getColor(this, com.jpardogo.android.googleprogressbar.library.R.color.red))
        snackbar.show()

    }


}