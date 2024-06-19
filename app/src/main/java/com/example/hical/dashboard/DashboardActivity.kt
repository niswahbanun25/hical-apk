package com.example.hical.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hical.R
import com.example.hical.ViewModelFactory
import com.example.hical.adapter.DashboardItemAdapter
import com.example.hical.analisis.AnalisisActivity
import com.example.hical.api.Result
import com.example.hical.databinding.ActivityDashboardBinding
import com.example.hical.response.DashboardItem
import com.example.hical.response.DeletePayload
import com.example.hical.welcome.WelcomeActivity

class DashboardActivity : AppCompatActivity() {
    private val viewModel by viewModels<DashboardViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDashboardBinding
    private val dashboardItemAdapter = DashboardItemAdapter()
    private lateinit var loadingDialog: AlertDialog

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val intent = Intent(this, AnalisisActivity::class.java)
                intent.putExtra(AnalisisActivity.EXTRA_IMAGE, it.toString())
                startActivity(intent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val inflaterLoad: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(this)
            .setView(inflaterLoad.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(true)
        loadingDialog = loadingAlert.create()

        enableEdgeToEdge()

        observeViewModel()
        setRecyclerView()
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            icUpload.setOnClickListener {
                openGallery()
            }
            imgLogout.setOnClickListener {
                showLogoutConfirmationDialog()
            }

            dashboardItemAdapter.onItemDeleteClick = { dashboardItem ->
                val builder = AlertDialog.Builder(this@DashboardActivity)

                with(builder)
                {
                    setTitle("Warning !")
                    setMessage("Are you sure to delete ${dashboardItem.meal} by the number of calories${dashboardItem.amount}?")
                    setPositiveButton(
                        "Yes"
                    ) { dialog, _ ->
                        viewModel.deleteItem(DeletePayload(dashboardItem.id))
                            .observe(this@DashboardActivity) { result ->
                                when (result) {
                                    is Result.Loading -> {
                                        loadingDialog.show()
                                    }

                                    is Result.Success -> {
                                        viewModel.fetchDashboardItems()
                                        loadingDialog.dismiss()
                                    }

                                    is Result.Error -> {
                                        loadingDialog.dismiss()
                                        showToast(result.error)
                                    }

                                    else -> {}
                                }
                            }
                    }
                    setNegativeButton(
                        "No"
                    ) { dialog, _ -> dialog.dismiss() }
                    show()
                }
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Logout !")
        builder.setMessage("Are you sure you want to exit the application?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            logout()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun observeViewModel() {
        viewModel.apply {
            isLoading.observe(this@DashboardActivity, ::showLoading)
            errorMessage.observe(this@DashboardActivity, ::showToast)

            dashboardItem.observe(this@DashboardActivity) { dashboardItemList ->
                dashboardItemAdapter.submitList(dashboardItemList)
                updateTotalCals(dashboardItemList)
            }
        }
    }

    private fun setRecyclerView() {
        binding.rvDashboardItems.apply {
            adapter = dashboardItemAdapter
            layoutManager = LinearLayoutManager(this@DashboardActivity)
        }
    }

    private fun updateTotalCals(dashboardItemList: ArrayList<DashboardItem>?) {
        val totalAmountCals = dashboardItemList?.sumOf { it.amount ?: 0 } ?: 0
        binding.tvAmountTotal.text = totalAmountCals.toString()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressbar.isVisible = isLoading
            layoutDashboardItems.isVisible = !isLoading
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }
    private fun logout() {
        // Hapus data autentikasi dari SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Arahkan ke WelcomeActivity
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
