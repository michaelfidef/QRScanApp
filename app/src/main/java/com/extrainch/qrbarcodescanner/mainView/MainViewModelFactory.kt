package com.extrainch.qrbarcodescanner.mainView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.extrainch.qrbarcodescanner.Repository

class MainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}