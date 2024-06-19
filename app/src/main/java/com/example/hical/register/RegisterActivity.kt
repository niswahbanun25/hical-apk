package com.example.hical.register


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import com.example.hical.databinding.ActivityRegisterBinding
import com.example.hical.loginpage.LoginActivity
import com.example.hical.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class RegisterActivity : AppCompatActivity() {

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        binding.btnRegist2.setOnClickListener {
            val name = binding.namaEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            lifecycleScope.launch {
                try {
                    showLoading(true)
                    val successResponse = viewModel.register(name, email, password)
                    successResponse.message?.let { it1 -> showToast(it1) }

                    AlertDialog.Builder(this@RegisterActivity).apply {
                        setTitle("Welcome $name")
                        setMessage("Your account has been created, welcome to Hi Cal!")
                        setPositiveButton("Continue") { _, _ ->
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()
                        }
                        create()
                        show()
                    }
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                    errorResponse.message?.let { it1 -> showToast(it1) }
                } finally {
                    showLoading(false)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView9, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleRegister = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 0f, 1f).setDuration(500)
        val nameSignup = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 0f, 1f).setDuration(500)
        val nameEditSignup = ObjectAnimator.ofFloat(binding.namaEditText, View.ALPHA, 0f, 1f).setDuration(500)
        val emailSignup = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(500)
        val emailEditSignup = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 0f, 1f).setDuration(500)
        val passwordSignup = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(500)
        val passwordEditSignup = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 0f, 1f).setDuration(500)
        val signupButton = ObjectAnimator.ofFloat(binding.btnRegist2, View.ALPHA, 0f, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(signupButton)
        }

        AnimatorSet().apply {
            playSequentially(
                titleRegister, nameSignup, nameEditSignup,
                emailSignup, emailEditSignup, passwordSignup,
                passwordEditSignup, together
            )
            startDelay = 500 // Optional start delay before starting the animation
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
