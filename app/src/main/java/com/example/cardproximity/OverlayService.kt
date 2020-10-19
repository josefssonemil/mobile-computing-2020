package com.example.cardproximity

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.cardproximity.sound.SoundHandler
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.custom_dialog_view_layout.view.*
import kotlin.system.exitProcess

class OverlayService : Service(), View.OnClickListener {

    // Fixed coordinates for proof of concept
    private var cardLocationLat: Double = 57.705476
    private var cardLocationLon: Double = 12.026175
    private var phoneLocationLat: Double = 57.705476
    private var phoneLocationLon: Double = 12.026175

    private lateinit var params: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager
    private lateinit var dialog: CustomLayout
    private lateinit var soundHandler: SoundHandler
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var longitude = 0.0
    private var latitude = 0.0

    override fun onCreate() {
        super.onCreate()

        Log.i("overlay", "***BUILD ID*** "+Build.ID)

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        dialog = CustomLayout(this, null)
        dialog.cancel_button.setOnClickListener(this)

        soundHandler = SoundHandler()

        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.BOTTOM or Gravity.END

        windowManager.addView(dialog, params)

        checkProximity()
    }

    private fun initiateSound() {
        soundHandler.start()
    }

    private fun checkSound(): Boolean {
        return soundHandler.checkStatus()
    }

    private fun stopSound() {

        // wait a little bit before listening
        Thread.sleep(5000)
        soundHandler.stop()
    }

    private fun checkProximity() {
        val request = LocationRequest()
        // Intervals if continious updating needed
//        request.interval = 10000
//        request.fastestInterval = 5000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if (checkPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(
                request,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        latitude = locationResult.lastLocation.latitude
                        longitude = locationResult.lastLocation.longitude

                        if (isInProximity(phoneLocationLat, phoneLocationLon)) {
                            Log.i("overlay", "LOCATION PROXIMITY ACCEPTED")

                            Log.i("overlay", "sound initiated")
                            initiateSound()

                            if (checkSound()) {
                                setAcceptedData()
                                stopSound()
                                Log.i("overlay", "SOUND PROXIMITY ACCEPTED")

                                // Payment complete
                            } else {
                                setRejectedData()
                                stopSound()
                                Log.i("overlay", "SOUND PROXIMITY REJECTED")
                            }
                        } else {
                            setRejectedData()
                            Log.i("overlay", "LOCATION PROXIMITY REJECTED")
                        }
                    }
                },
                null
            )
        }
    }

    private fun checkPermission(): Boolean {

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("overlay", "PERMISSION GRANTED")
            return true
        }
        Log.i("location", "PERMISSION DENIED")
        return false
    }

    /* Calculates coordinates distance in meters and returns true or false depending on a set difference in meters*/
    fun isInProximity(latitude: Double, longitude: Double): Boolean {
        val boundary = 10
        val earthRadius = 6378.137

        val dLat = latitude * Math.PI / 180 - cardLocationLat * Math.PI / 180
        val dLon = longitude * Math.PI / 180 - cardLocationLon * Math.PI / 180
        val a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(cardLocationLat * Math.PI / 180) * Math.cos(
                latitude * Math.PI / 180
            ) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distanceInMeters = earthRadius * c * 1000

        if (distanceInMeters <= boundary) {
            Log.i("overlay", "in proximity [" + Math.round(distanceInMeters) + "m]")
            return true
        } else {
            Log.i(
                "overlay", "NOT in proximity [" + Math.round(distanceInMeters) + "m]"
            )
            return false
        }
    }


    private fun setAcceptedData() {
        dialog.title_text.text = "Payment Accepted"
        dialog.info_text.text = "Proximity accepted"
        dialog.progress_circular.visibility = View.GONE
        dialog.cancel_button.text = "CLOSE"
    }

    private fun setRejectedData() {
        dialog.title_text.text = "Payment Rejected"
        dialog.info_text.text = "Proximity rejected"
        dialog.progress_circular.visibility = View.GONE
        dialog.cancel_button.text = "CLOSE"
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onClick(p0: View?) {
        exitProcess(0)
    }
}