package com.extrainch.qrbarcodescanner.retrofit


import com.extrainch.qrbarcodescanner.BarcodeResponse
import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.POST

interface ApiService {
    @POST("/validate")
    fun validasi(
        @Body request: String
    ): Call<BarcodeResponse>

}