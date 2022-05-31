package com.example.braindiction.ui.patient

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.braindiction.databinding.ActivityDetailPatientBinding
import com.example.braindiction.ui.archive.ArchiveActivity
import com.example.braindiction.ui.createTempFile
import com.example.braindiction.ui.uriToFile
import java.io.File

class DetailPatientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPatientBinding
    private var isAllFabsVisible: Boolean = false
    private var getFile: File? = null

    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        supportActionBar?.title = "Detail Patient"

        fabAddAction()
        setupAction()
    }

    private fun setupAction(){
        binding.apply {
            fabScan.setOnClickListener { startTakePhoto() }
            fabGallery.setOnClickListener { startGallery() }
            // fabPredict.setOnClickListener { uploadImage() }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
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

            // binding.previewImageView.setImageBitmap(result)
        }
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            // binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun fabAddAction(){
        binding.apply {
            //button
            fabGallery.visibility = View.GONE
            fabScan.visibility = View.GONE
            fabPredict.visibility = View.GONE

            //text
            addScanButton.visibility = View.GONE
            addImportImage.visibility = View.GONE
            seePrediction.visibility = View.GONE

            fabAction.shrink()
            fabAction.setOnClickListener {
                if (!isAllFabsVisible) {

                    fabScan.show()
                    fabGallery.show()
                    fabPredict.show()

//                    addScanButton.setHint(View.VISIBLE)
//                    addImportImage.setHint(View.VISIBLE)
//                    seePrediction.setHint(View.VISIBLE)

                    fabAction.extend()

                    isAllFabsVisible = true

                } else {

                    fabScan.hide()
                    fabGallery.hide()
                    fabPredict.hide()

//                    addScanButton.setHint(View.GONE)
//                    addImportImage.setHint(View.GONE)
//                    seePrediction.setHint(View.GONE)

                    fabAction.shrink()

                    isAllFabsVisible = false
                }
            }
            fabScan.setOnClickListener {
                Toast.makeText(
                    this@DetailPatientActivity, "App is scanning",
                    Toast.LENGTH_SHORT
                ).show()
            }

            fabGallery.setOnClickListener {
                Toast.makeText(
                    this@DetailPatientActivity, "Opening gallery",
                    Toast.LENGTH_SHORT
                ).show()
            }

            fabPredict.setOnClickListener {
                Toast.makeText(
                    this@DetailPatientActivity, "Predicting the x-ray",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        val backTo = Intent(this, ArchiveActivity::class.java)
        startActivity(backTo)
        return true
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}