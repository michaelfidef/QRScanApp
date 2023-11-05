package com.extrainch.qrbarcodescanner

import androidx.lifecycle.ViewModel
import okhttp3.RequestBody

class MainViewModel(private val repository: Repository) : ViewModel()  {
    fun getKode(kode : RequestBody) = repository.uploadKode(kode)
}