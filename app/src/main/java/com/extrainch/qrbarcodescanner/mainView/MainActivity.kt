package com.extrainch.qrbarcodescanner.mainView

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.extrainch.qrbarcodescanner.Repository
import com.extrainch.qrbarcodescanner.ResultState
import com.extrainch.qrbarcodescanner.databinding.ActivityMainBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    private var barcodeDetector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null
    private var toneGen1: ToneGenerator? = null
    private var barcodeData: String? = null
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        initialiseDetectorsAndSources()
    }

    private fun initialiseDetectorsAndSources() {
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        binding!!.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraSource?.start(binding!!.surfaceView.holder)
                    } else {
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop()
            }
        })


        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {

            }

            override fun receiveDetections(detections: Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() != 0) {
                    binding!!.barcodeText.post {
                        if (barcodes.valueAt(0).email != null) {
                            binding!!.barcodeText.removeCallbacks(null)
                            barcodeData = barcodes.valueAt(0).email.address
                            binding!!.barcodeText.text = barcodeData
                            Log.d(TAG, "barcode: ${barcodeData} ")
                            toneGen1!!.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
                        } else {
                            Log.d(TAG, "barcode: ${barcodeData} ")
                            //parsing
                            val parts = barcodeData.toString().split("*")
                            if (parts.size > 1) {
                                val parsedString = parts[1]
                                Log.d(TAG, "kode: ${parsedString}")
                                val json = """
                                        {
                                            "kode_unik": "${parsedString}"
                                        }
                                        """.trimIndent()
                                val mediaType = "application/json; charset=utf-8".toMediaType()
                                val requestBody = json.toRequestBody(mediaType)
                                viewModel.getKode(requestBody)
                                    .observe(this@MainActivity) { result ->
                                        if (result != null) {
                                            when (result) {

                                                is ResultState.Success -> {
                                                    val response = result.data
                                                    Log.d(TAG, "receiveDetections: $response")
                                                    AlertDialog.Builder(this@MainActivity).apply {
                                                        setTitle("BERHASIL!")
                                                        setMessage("kode barcode anda benar")
                                                        setPositiveButton("Ok") { _, _ ->
                                                        }
                                                        show()
                                                    }
                                                }

                                                is ResultState.Error -> {
                                                    showToast(result.error)
                                                    Log.e(
                                                        TAG,
                                                        "receiveDetections: ${result.error}",
                                                    )
                                                }

                                                else -> {
                                                    AlertDialog.Builder(this@MainActivity).apply {
                                                        setTitle("KODE SUDAH TERPAKAI!")
                                                        setMessage("maaf kode tidak valid!")
                                                        setPositiveButton("Ok") { _, _ ->
                                                        }
                                                        show()
                                                    }
                                                }
                                            }
                                        }
                                    }
                            } else {
                                AlertDialog.Builder(this@MainActivity).apply {
                                    setTitle("SALAH!")
                                    setMessage("kode barcode anda salah")
                                    setPositiveButton("Ok") { _, _ ->
                                    }
                                    show()
                                }
                            }
                            barcodeData = barcodes.valueAt(0).displayValue
                            binding!!.barcodeText.text = barcodeData
                            toneGen1!!.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
                        }
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        supportActionBar!!.hide()
        cameraSource!!.release()
    }

    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
        initialiseDetectorsAndSources()
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 201
        val TAG = "MainActivity"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

