package com.example.hical.calculation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hical.R
import com.example.hical.ViewModelFactory
import com.example.hical.adapter.MealsFormAdapter
import com.example.hical.dashboard.DashboardActivity
import com.example.hical.databinding.ActivityCalculationBinding

class CalculationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculationBinding

    private val mealsFormAdapter = MealsFormAdapter()

    private val viewModel by viewModels<CalculationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupView()
        applyAnimations()
        observeViewModel()
        setMealsFormRecyclerView()
        setListeners()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun applyAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in)

        binding.imageView8.startAnimation(fadeIn)
        binding.textTittleCalculation.startAnimation(slideIn)
    }

    private fun observeViewModel() {
        viewModel.apply {
            isLoading.observe(this@CalculationActivity, ::showLoading)
            errorMessage.observe(this@CalculationActivity, ::showToast)

            addItemResponse.observe(this@CalculationActivity) {
                if (it != null) {
                    finishAffinity()
                    startActivity(Intent(this@CalculationActivity, DashboardActivity::class.java))
                }
            }
        }
    }

    private fun setMealsFormRecyclerView() {
        binding.rvMealsForm.apply {
            adapter = mealsFormAdapter
            layoutManager = LinearLayoutManager(this@CalculationActivity)
        }
    }

    private fun setListeners() {
        binding.apply {
            btnSave.setOnClickListener {
                val editTextValues = mealsFormAdapter.getInputtedEditText()

                if (validate(editTextValues)) {
                    val meal = editTextValues[0]?.get(0) ?: ""
                    val cals = editTextValues[0]?.get(1) ?: "0"

                    viewModel.addMealsItems(meal, cals.toInt())
                }
            }

            imagehome.setOnClickListener {
                val intent = Intent(this@CalculationActivity, DashboardActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validate(editTextValues: MutableMap<Int, ArrayList<String>>) =
        if (editTextValues.isEmpty()) {
            showToast("Isi form terlebih dahulu!")
            false
        } else if (editTextValues[0]?.get(0).isNullOrEmpty()) {
            showToast("Masukkan Meal terlebih dahulu!")
            false
        } else if (editTextValues[0]?.get(1).isNullOrEmpty()) {
            showToast("Masukkan Calories terlebih dahulu!")
            false
        } else {
            true
        }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressbar.isVisible = isLoading
            btnSave.isVisible = !isLoading
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
