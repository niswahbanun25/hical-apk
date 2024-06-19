package com.example.hical.analisis

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hical.R
import com.example.hical.ViewModelFactory
import com.example.hical.api.Result
import com.example.hical.databinding.ActivityAnalisisBinding
import com.example.hical.upload.ResultActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class AnalisisActivity : AppCompatActivity() {
    private lateinit var imagePathLocation: String
    private lateinit var loadingDialog: AlertDialog

    private lateinit var binding: ActivityAnalisisBinding
    private val viewModel by viewModels<AnalisisViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val selectedImageUri by lazy {
        intent.getStringExtra(EXTRA_IMAGE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalisisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        val inflaterLoad: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(this)
            .setView(inflaterLoad.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(true)
        loadingDialog = loadingAlert.create()

        selectedImageUri?.let {
            val image = reduceImageSize(uriFileConverter(Uri.parse(it)))
            viewModel.imageFile.postValue(image)
        }

        observeViewModel()

        binding.btnAnalisis.setOnClickListener {
            if (selectedImageUri == null) {
                showToast("Add Image first!")
            } else {
                viewModel.analyzeImage(viewModel.imageFile.value!!)
                    .observe(this@AnalisisActivity) { result ->
                        when (result) {
                            is Result.Loading -> {
                                loadingDialog.show()
                            }

                            is Result.Success -> {
                                loadingDialog.dismiss()
                                val intent =
                                    Intent(this@AnalisisActivity, ResultActivity::class.java)
                                intent.putExtra(EXTRA_IMAGE, selectedImageUri)
                                intent.putExtra(ResultActivity.EXTRA_ANALYZE, result.data)
                                startActivity(intent)
                                finish()
                            }

                            is Result.Error -> {
                                loadingDialog.dismiss()
                                showToast(result.error)
                            }
                        }
                    }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            imageFile.observe(this@AnalisisActivity) {
                binding.capturedImage.setImageBitmap(BitmapFactory.decodeFile(it.path))
            }
        }
    }

    private fun uriFileConverter(uri: Uri): File {
        val tempFile = File.createTempFile(
            SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(System.currentTimeMillis()),
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        ).also { imagePathLocation = it.absolutePath }

        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return tempFile
    }

    private fun reduceImageSize(file: File): File {
        val maxFileSize = 1000000
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var outputStream: FileOutputStream? = null

        try {
            while (compressQuality > 0) {
                val bmpStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
                val bmpPicByteArray = bmpStream.toByteArray()

                if (bmpPicByteArray.size <= maxFileSize) {
                    outputStream = FileOutputStream(file)
                    outputStream.write(bmpPicByteArray)
                    outputStream.flush()
                    break
                }

                compressQuality -= 5
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputStream?.close()
        }

        return file
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
    }
}
