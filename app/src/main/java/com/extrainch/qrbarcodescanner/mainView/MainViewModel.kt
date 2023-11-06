package com.extrainch.qrbarcodescanner.mainView

import androidx.lifecycle.ViewModel
import com.extrainch.qrbarcodescanner.Repository
import okhttp3.RequestBody

class MainViewModel(private val repository: Repository) : ViewModel()  {
    fun getKode(kode : RequestBody) = repository.uploadKode(kode)
}