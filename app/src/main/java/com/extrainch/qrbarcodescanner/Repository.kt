package com.extrainch.qrbarcodescanner

import com.google.gson.Gson
import retrofit2.HttpException
import androidx.lifecycle.liveData
import com.extrainch.qrbarcodescanner.retrofit.ApiConfig
import okhttp3.RequestBody

class Repository {
    fun uploadKode(request: RequestBody) = liveData {
//        emit(ResultState.Loading)
        try {
            val successResponse = ApiConfig.getApiConfig().validasi(request)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, BarcodeResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }
}