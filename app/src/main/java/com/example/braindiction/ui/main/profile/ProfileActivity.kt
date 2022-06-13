package com.example.braindiction.ui.main.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.braindiction.R
import com.example.braindiction.api.UserRegister
import com.example.braindiction.databinding.ActivityProfileBinding
import com.example.braindiction.ui.main.home.HomeActivity
import com.example.braindiction.ui.main.notification.NotificationActivity
import com.example.braindiction.ui.main.settings.SettingsActivity
import com.example.braindiction.ui.uriToFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var userProfile: UserRegister
    private lateinit var storage: FirebaseStorage
    private lateinit var userId: String

    private var getFile: File? = null

    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Profile"

        database = Firebase.database.getReference("User Doctor Register")
        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        storage = Firebase.storage

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
                        evAddress.text = userProfile.address
                        evEmail.text = userProfile.email

                        val formatSQL = SimpleDateFormat("yyyy-MM-dd")
                        val sdfConvert = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
                        val dateValue = formatSQL.parse(userProfile.dob.toString()) as Date
                        val dobPatient = sdfConvert.format(dateValue)
                        evDob.text = dobPatient
                    }

                    override fun onCancelled(error: DatabaseError) {
                        showLoading(false)
                        Toast.makeText(this@ProfileActivity, "Something's error happened!", Toast.LENGTH_SHORT).show()
                    }
                })

            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null){
                Glide.with(this@ProfileActivity)
                    .load(currentUser.photoUrl)
                    .circleCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imgProfileAvatar)
            }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    fun actionPP(view: View) {
        AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.title_profile_action))
            setPositiveButton(getString(R.string.profile_pb2)) { _, _ ->
                startTakePhoto()
            }
            setNegativeButton(getString(R.string.probile_pb3)) { _, _ ->
                startGallery()
            }
            create()
            show()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        com.example.braindiction.ui.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.example.braindiction",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)

            Glide.with(this)
                .load(result)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgProfileAvatar)

            uploadToFirebase(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            val imageUri: Uri = selectedImg
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, Uri.parse(
                imageUri.toString()
            ))
            Glide.with(this)
                .load(result)
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgProfileAvatar)

            uploadToFirebase(bitmap)
            }
        }

    private fun uploadToFirebase(bitmap: Bitmap) {
        val profilePicture = storage.reference.child("userProfilImage")
            .child("$userId.jpeg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val data = baos.toByteArray()

        val uploadTask = profilePicture.putBytes(data)
        uploadTask.addOnFailureListener {
            Log.e(TAG, "onFailure : ${it.cause}")
            Toast.makeText(this,"Failed uploading to server.",Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            displayProfilePicture(profilePicture)
        }
    }

    private fun displayProfilePicture(profilePicture: StorageReference){
        profilePicture.downloadUrl
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "onSuccess : $task")
                getDPUploaded(task.result)
            }
        }
    }

    private fun getDPUploaded(uri: Uri) {
        showLoading(true)
        val currentUser = FirebaseAuth.getInstance().currentUser

        val req = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri).build()

        val uploadTask = currentUser?.updateProfile(req)
        uploadTask?.addOnSuccessListener {
            showLoading(false)
            Toast.makeText(this, "Profile photo is successfully updated!", Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener {
            showLoading(false)
            Toast.makeText(this, "Profile photo failed.", Toast.LENGTH_SHORT).show()
            }

        }

    companion object {
        const val TAG = "ProfilePicture"
    }
}