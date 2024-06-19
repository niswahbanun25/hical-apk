package com.example.hical.upload

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hical.R
import com.example.hical.analisis.AnalisisActivity
import com.example.hical.calculation.CalculationActivity
import com.example.hical.databinding.ActivityResultBinding
import com.example.hical.response.AnalyzeResponse

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private val selectedImageUri by lazy {
        intent.getStringExtra(AnalisisActivity.EXTRA_IMAGE)
    }

    private val analyzeResponse by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_ANALYZE)
        } else {
            intent.getParcelableExtra(EXTRA_ANALYZE, AnalyzeResponse::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setViews()

        binding.button8.setOnClickListener {
            navigateToCalculationActivity()
        }
    }

    private fun setViews() {
        binding.apply {
            selectedImageUri?.let {
                imageView4.setImageURI(Uri.parse(it))
            }
            tvLabel.text = analyzeResponse?.label
            tvInfo.text = analyzeResponse?.information
        }
    }

    private fun navigateToCalculationActivity() {
        val intent = Intent(this, CalculationActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_ANALYZE = "extra_analyze"
    }
}