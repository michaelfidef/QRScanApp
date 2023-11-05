package com.extrainch.qrbarcodescanner

import androidx.lifecycle.ViewModel

class MainViewModel(private val repository: Repository) : ViewModel()  {
    fun getKode(kode : String) = repository.uploadKode(kode)
}