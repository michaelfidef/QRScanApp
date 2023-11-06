package com.extrainch.qrbarcodescanner.retrofit


import com.extrainch.qrbarcodescanner.BarcodeResponse
import okhttp3.RequestBody
import retrofit2.http.Body

import retrofit2.http.POST

interface ApiService {
    @POST("validate")
    suspend fun validasi(
        @Body request: RequestBody
    ): BarcodeResponse
}