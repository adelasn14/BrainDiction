package com.example.braindiction.ui.prediction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.braindiction.databinding.ActivityPredictionBinding

class PredictionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPredictionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}