package com.example.braindiction.ui.prediction

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.braindiction.api.ApiConfig
import com.example.braindiction.api.UploadResponse
import com.example.braindiction.databinding.ActivityPredictionBinding
import com.example.braindiction.ui.patient.DetailPatientActivity
import com.example.braindiction.ui.reduceFileImage
import com.example.braindiction.ui.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class PredictionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPredictionBinding
    private var isAllFabsVisible: Boolean = false
    private var getFile: File? = null

    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        fabAddAction()

        binding.apply {
            fabScan.setOnClickListener{startTakePhoto()}
            fabGallery.setOnClickListener{startGallery() }
            predictButton.setOnClickListener { uploadXray() }
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
        else {
            val backToDetail = Intent(this, DetailPatientActivity::class.java)
            startActivity(backToDetail)
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

            binding.apply {
                previewImageView.setImageBitmap(result)

                predictTv.visibility = View.VISIBLE
                predictResultTv.visibility = View.VISIBLE
                predictButton.visibility = View.VISIBLE
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            binding.apply {
                previewImageView.setImageURI(selectedImg)

                predictButton.visibility = View.VISIBLE
            }
        }
    }

    private fun uploadXray() {
        val _predictResult = MutableLiveData<UploadResponse>()
        val predictResult: LiveData<UploadResponse> = _predictResult

        if (getFile != null) {

            val file = reduceFileImage(getFile as File)

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestImageFile)


            val service = ApiConfig().getApiServiceML().uploadXray(
                imageMultipart
            )
            service.enqueue(object : Callback<UploadResponse> {
                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
                ) {
                    showLoading(true)
                    if (response.isSuccessful) {
                        binding.fabAction.shrink()
                        val responseBody = response.body()
                        if (responseBody != null) {
                            Log.d("PredictionActivity", responseBody.toString())
                            showLoading(false)
                            binding.apply {
                                predictResultTv.text = StringBuilder().append("Percentage : ").append(responseBody.percentage.toString()).append("%").append("\n").append("Prediction : ").append(responseBody.prediction)
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@PredictionActivity,
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    Toast.makeText(
                        this@PredictionActivity,
                        "Gagal instance Retrofit",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                this@PredictionActivity,
                "Silakan masukkan berkas gambar terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.predictTv.visibility = View.VISIBLE
        showLoading(true)
        binding.predictResultTv.visibility = View.VISIBLE
        showLoading(false)
    }

    private fun fabAddAction(){
        binding.apply {
            //button
            fabGallery.visibility = View.GONE
            fabScan.visibility = View.GONE

            //text
            addScanButton.visibility = View.GONE
            addImportImage.visibility = View.GONE


            fabAction.shrink()
            fabAction.setOnClickListener {
                if (!isAllFabsVisible) {

                    fabScan.show()
                    fabGallery.show()

                    addScanButton.visibility = View.VISIBLE
                    addImportImage.visibility = View.VISIBLE

                    fabAction.extend()

                    isAllFabsVisible = true

                } else {

                    fabScan.hide()
                    fabGallery.hide()

                    addScanButton.visibility = View.GONE
                    addImportImage.visibility = View.GONE

                    fabAction.shrink()

                    isAllFabsVisible = false
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        const val EXTRA_ID = "extra_id"

    }

}