package com.example.hical.loginpage

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hical.ViewModelFactory
import com.example.hical.dashboard.DashboardActivity
import com.example.hical.databinding.ActivityLoginBinding
import com.example.hical.main.MainActivity
import com.example.hical.preference.UserModel
import com.example.hical.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupView()
        setupAction()
        playAnimation()
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

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            lifecycleScope.launch {
                try {
                    showLoading(true)
                    val successResponse = viewModel.login(email, password)
                    val token = successResponse.token ?: ""
                    viewModel.saveSession(UserModel(email, token))

//                    successResponse.message?.let { it1 -> showToast(it1) }

                    AlertDialog.Builder(this@LoginActivity).apply {
                        setTitle("Welcome Back")
                        setMessage("Let's continue our journey!")
                        setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                } catch (e: HttpException) {
                    handleHttpException(e)
                } catch (e: Exception) {
                    Log.e("LoginError", "Unexpected error: ${e.message}", e)
                    showToast("An unexpected error occurred. Please try again.")
                } finally {
                    showLoading(false)
                }
            }
        }
    }


    private fun handleHttpException(e: HttpException) {
        try {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            // Menampilkan pesan khusus jika gagal login
            showToast("email dan password invalid, try again!")
        } catch (ex: Exception) {
            Log.e("LoginError", "Error parsing response: ${ex.message}", ex)
            showToast("Login failed. Please try again.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.imageView7, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        // TextView and Button animations
        val image = ObjectAnimator.ofFloat(binding.imageView7, View.ALPHA, 0f, 1f).setDuration(500)
        val email= ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(500)
        val editTextEmail = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 0f, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(500)
        val editTextPassword = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 0f, 1f).setDuration(500)
        val button = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 0f, 1f).setDuration(500)

        val buttonSet = AnimatorSet().apply {
            playTogether(button)
        }

        AnimatorSet().apply {
            playSequentially( image, email, editTextEmail, password, editTextPassword, buttonSet)
            startDelay = 500 // Optional start delay before starting the animation
            start()
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
