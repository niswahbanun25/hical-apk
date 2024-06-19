package com.example.hical.analisis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hical.repository.RetrofitRepository
import java.io.File

class AnalisisViewModel(private val repository: RetrofitRepository) : ViewModel() {
    val imageFile = MutableLiveData<File>()

    fun analyzeImage(
        file: File
    ) = repository.analyzeImage(
        file
    )
}