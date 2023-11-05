package com.extrainch.qrbarcodescanner.retrofit


import com.extrainch.qrbarcodescanner.BarcodeResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.POST

interface ApiService {
    @POST("/validate")
    fun validasi(
        @Body request: RequestBody
    ): Call<BarcodeResponse>

}