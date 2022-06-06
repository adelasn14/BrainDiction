package com.example.braindiction.ui.prediction

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.braindiction.databinding.ActivityPredictionBinding


class PredictionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPredictionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(EXTRA_SCAN)) {
            val b = BitmapFactory.decodeByteArray(
                intent.getByteArrayExtra(EXTRA_SCAN),
                0,
                intent.getByteArrayExtra(EXTRA_SCAN)!!.size
            )
            binding.previewImageView.setImageBitmap(b)
        }
    }

    companion object {
        const val EXTRA_SCAN = "extra_scan"
        const val EXTRA_GALLERY = "extra_gallery"
    }

}