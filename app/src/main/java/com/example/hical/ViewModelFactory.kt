package com.example.hical

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hical.analisis.AnalisisViewModel
import com.example.hical.calculation.CalculationViewModel
import com.example.hical.dashboard.DashboardViewModel
import com.example.hical.di.Injection
import com.example.hical.loginpage.LoginViewModel
import com.example.hical.main.MainViewModel
import com.example.hical.register.RegisterViewModel
import com.example.hical.repository.RetrofitRepository

class ViewModelFactory(
    private val retrofitRepository: RetrofitRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(retrofitRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(retrofitRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(retrofitRepository) as T
            }

            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(retrofitRepository) as T
            }

            modelClass.isAssignableFrom(CalculationViewModel::class.java) -> {
                CalculationViewModel(retrofitRepository) as T
            }

            modelClass.isAssignableFrom(AnalisisViewModel::class.java) -> {
                AnalisisViewModel(retrofitRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
//        @Volatile
//        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return ViewModelFactory(Injection.provideRetrofitRepository(context))
//            if (INSTANCE == null) {
//                synchronized(ViewModelFactory::class.java) {
//                    val retrofitRepository = Injection.provideRetrofitRepository(context)
//                    INSTANCE = ViewModelFactory(retrofitRepository)
//                }
//            }
//            return INSTANCE as ViewModelFactory
        }
    }
}