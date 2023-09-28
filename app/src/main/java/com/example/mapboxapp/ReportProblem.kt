package com.example.mapboxapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mapboxapp.dataclass.issues
import com.example.mapboxapp.dataclass.problems
import com.example.mapboxapp.ml.ModelUnquant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ReportProblem : AppCompatActivity() {

    //    var types = arrayOf<String?>("C", "Data structures",
//        "Interview prep", "Algorithms",
//        "DSA with java", "OS")
    private lateinit var dbref1: DatabaseReference
    private lateinit var dbref2: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mapButton: ImageView
    private var city: String = ""
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var problemType: String = "none"
    private var imgUri: Uri? = null
    private lateinit var bitmap: Bitmap
    private lateinit var tvOutput: TextView
    private lateinit var pd: ProgressDialog
    private lateinit var text:TextView
    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) {

        if (it != null) {
            imgUri = it
            bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(it))
            val ImgView: ImageView = findViewById(R.id.selectImage)
            ImgView.setImageURI(it)
            outputGenerator(bitmap)

        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200) {
            if (resultCode == 200) {
                Toast.makeText(this,"Finished Caught",Toast.LENGTH_SHORT).show()
                lat = data?.getDoubleExtra("latitude",0.0)!!
                long = data?.getDoubleExtra("longitude",0.0)!!// Replace "key" with the same key you used in the second activity.
                city = data.getStringExtra("address")!!// Handle the received data here.
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"CANCELLED",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_problem)
        mapButton=findViewById(R.id.mapButton)

        mapButton.setOnClickListener {
            val intent = Intent(this, LocationPickerActivity::class.java)
            startActivityForResult(intent,200) }
//        val problemTypes = arrayOf(
//            "Urban Flooding",
//            "Rural Flooding",
//            "Oil Spill",
//            "Tsunami",
//            "Polluted River",
//            "Drought",
//            "Drainage problems"
//        )

        val dropDownMenu : Button = findViewById(R.id.problemType)
        tvOutput = findViewById(R.id.tvType)
        var checkedItemId : Int = 0

        dropDownMenu.setOnClickListener {
            val popupMenu= PopupMenu(applicationContext,dropDownMenu)
            popupMenu.menuInflater.inflate(R.menu.problem_types_menu,popupMenu.menu)
            if (checkedItemId!=0)
                popupMenu.menu.findItem(checkedItemId).isChecked=true

            popupMenu.setOnMenuItemClickListener {item ->
                item.isChecked = true
                checkedItemId=item.itemId
                problemType=item.title.toString()
                dropDownMenu.text = problemType

                true
            }
            popupMenu.show()
        }

        val insertImage : ImageView = findViewById(R.id.selectImage)
        insertImage.setOnClickListener{
            selectImage.launch("image/*")

        }

        firebaseAuth = FirebaseAuth.getInstance()
        //val username: String = firebaseAuth.currentUser!!.uid.toString()
        val username = "abhinavkr327"
        dbref1 = FirebaseDatabase.getInstance().getReference("user").child(username).child("problems")
        dbref2 = FirebaseDatabase.getInstance().getReference("problems")
        storageRef = FirebaseStorage.getInstance().reference


        val titleET: EditText = findViewById(R.id.titleEditText)
        val expectedLossET : EditText = findViewById(R.id.expectedLossEditText)
        val descriptionET : EditText = findViewById(R.id.descriptionEditText)



        val submitButton: Button = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            pd = ProgressDialog(this)
            pd.setMessage("Uploading...")
            pd.show()
            val title: String = titleET.text.toString()
            val expectedLoss: String = expectedLossET.text.toString()
            val descreption: String = descriptionET.text.toString()

            if(imgUri==null){
                Toast.makeText(this, "Please upload an image", Toast.LENGTH_LONG).show()
                pd.dismiss()
            }else{
                storageRef.child("images/${imgUri!!.lastPathSegment}").putFile(imgUri!!).addOnCompleteListener { it1 ->
                    if(it1.isSuccessful){
                        storageRef.child("images/${imgUri!!.lastPathSegment}").downloadUrl.addOnSuccessListener {imgUrl->
                            val problemsDataClass = problems(imgUrl.toString(), descreption, expectedLoss, title, problemType,
                                city!!, lat, long, username, null)
                            dbref1.setValue(problemsDataClass).addOnCompleteListener {
                                if(it.isSuccessful){
//                                    Toast.makeText(this, "Issue reported successfully", Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                            val issuesDataClass = issues(
                                imageURL = imgUrl.toString(),
                                description = descreption,
                                estimatedloss = expectedLoss,
                                title = title,
                                locationLat = lat,
                                locationLong = long,
                                city = city,
                                username = username,
                                type = problemType
                            )
                            dbref2.child(problemType).push().setValue(issuesDataClass).addOnCompleteListener {
                                if(it.isSuccessful)
                                    Toast.makeText(this, "Issue reported successfully", Toast.LENGTH_SHORT).show()
                                else Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                            //dbref2.child(problemType).child(city).child("users").child(username).setValue(true)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            pd.dismiss()
                        }
                        pd.dismiss()

                    } else{
                        pd.dismiss()
                    }

                }
            }
        }
    }




    private fun outputGenerator(bitmap: Bitmap) {


        val model = ModelUnquant.newInstance(this)

        var imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR)).build()

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)
        tensorImage = imageProcessor.process(tensorImage)


        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)


        inputFeature0.loadBuffer(tensorImage.buffer)


        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences: FloatArray = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }

        val classes = arrayOf(
            "Urban Flooding",
            "Rural Flooding",
            "Oil Spill",
            "Tsunami",
            "Polluted River",
            "Drought",
            "Drainage Problem"
        )
        tvOutput.text = classes[maxPos]
        problemType = classes[maxPos]
//        var s = ""
//        for (i in classes.indices) {
//            s += "${classes[i]}: ${String.format("%.1f%%", confidences[i] * 100)}\n"
//        }
//        tvOutput.text = s
// Releases model resources if no longer used.
        model.close()

    }

}