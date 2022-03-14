package com.university_project.fileupload

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    private lateinit var imgView: ImageView
    private lateinit var uploadBtn: Button
    val storeage=Firebase.storage
    private lateinit var imageUri:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgView = findViewById(R.id.img_imageView_Id)
        uploadBtn = findViewById(R.id.btn_uploadImage_id)

        var activityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res: ActivityResult ->
                if (res.resultCode == Activity.RESULT_OK) {
                   imageUri =Uri.parse(res.data?.data.toString())
                    Log.d("TAG", "onCreate: $imageUri")
                    val storageRef=storeage.reference.child("image/${System.currentTimeMillis()}")
                    storageRef.putFile(imageUri).addOnSuccessListener {
                       storageRef.downloadUrl.addOnSuccessListener {
                           Log.d("TAG", "onCreate: $it")
                       }
                        Log.d("TAG", "onCreate: Uploaded")
                        Toast.makeText(this,"imageIsUploaded",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{
                        Log.d("TAG", "onCreate: Failed ${it.message}")
                    }.addOnProgressListener {
                        Log.d("TAG", "onCreate: Uploading")
                    }
                }
            }
        uploadBtn.setOnClickListener {
            val intent = Intent()
            intent.type = ("image/*")
            intent.action = Intent.ACTION_GET_CONTENT
            activityResult.launch(intent)
        }
    }
}