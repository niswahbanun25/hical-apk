package com.example.hical.welcome


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hical.R
import com.example.hical.databinding.ActivityWelcomeBinding
import com.example.hical.loginpage.LoginActivity
import com.example.hical.register.RegisterActivity
import kotlin.math.sign

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
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
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegist.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        // TextView and Button animations
        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 0f, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 0f, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 0f, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.btnRegist, View.ALPHA, 0f, 1f).setDuration(500)

        val button = AnimatorSet().apply {
            playTogether(signup)
        }

        AnimatorSet().apply {
            playSequentially(title, desc, signup, login)
            startDelay = 300 // Optional start delay before starting the animation
            start()
        }
    }
}
