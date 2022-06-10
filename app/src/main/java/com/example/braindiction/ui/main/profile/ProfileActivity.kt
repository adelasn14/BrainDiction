package com.example.braindiction.ui.main.profile

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.braindiction.R
import com.example.braindiction.api.UserRegister
import com.example.braindiction.databinding.ActivityProfileBinding
import com.example.braindiction.ui.main.home.HomeActivity
import com.example.braindiction.ui.main.notification.NotificationActivity
import com.example.braindiction.ui.main.settings.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var userProfile: UserRegister
    private lateinit var storage: StorageReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Profile"

        database = Firebase.database.getReference("User Doctor Register")
        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

        if (userId.isNotEmpty())
        {
            getProfileData()
        }

        setupNavigation()
    }

    private fun getProfileData() {
        showLoading(true)
        binding.apply {
            database.child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        showLoading(false)
                        userProfile = snapshot.getValue(UserRegister::class.java)!!

                        evUsername.text = StringBuilder().append("＠").append(userProfile.username)
                        evName.text = userProfile.name
                        evPekerjaan.text = userProfile.occupation
                        evAddress.text = userProfile.address
                        evEmail.text = userProfile.email

                        val formatSQL = SimpleDateFormat("yyyy-MM-dd")
                        val sdfConvert = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
                        val dateValue = formatSQL.parse(userProfile.dob.toString()) as Date
                        val dobPatient = sdfConvert.format(dateValue)
                        evDob.text = dobPatient

                        getProfilePicture()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        showLoading(false)
                        Toast.makeText(this@ProfileActivity, "Something's error happened!", Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    private fun getProfilePicture() {
        showLoading(true)
        storage = FirebaseStorage.getInstance().reference.child("User Doctor Register/$userId.jpg")
        val localImage = File.createTempFile("tempImg", "jpg")
        storage.getFile(localImage).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localImage.absolutePath)
            binding.imageView.setImageBitmap(bitmap)
            showLoading(false)

        }.addOnFailureListener{
            showLoading(false)
            Toast.makeText(this, "Failed to retrieve profile picture", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupNavigation() {
        val bottomNav = binding.navView

        bottomNav.selectedItemId = R.id.navigation_profile

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> {
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_notification -> {
                    startActivity(Intent(applicationContext, NotificationActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_home -> {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }

    private fun setupUpdateProfile() {
        showLoading(true)
        storage.child("mountains.jpg")
        storage.child("images/mountains.jpg")

        val file = Uri.fromFile(File("path/to/images/rivers.jpg"))
        val riversRef = storage.child("images/${file.lastPathSegment}")
        val uploadTask = riversRef.putFile(file)

        uploadTask.addOnFailureListener {
            showLoading(false)
            Toast.makeText(this, "Failed to retrieve profile picture", Toast.LENGTH_SHORT).show()

        }.addOnSuccessListener { taskSnapshot ->
           
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}