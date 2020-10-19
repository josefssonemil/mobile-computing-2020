package com.example.cardproximity

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.custom_dialog_view_layout.view.*
import java.util.jar.Manifest

class OverlayService : Service(), View.OnClickListener {

    private lateinit var params: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager

    private lateinit var dialog: CustomLayout


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var longitude = 0.0
    private var latitude = 0.0

    override fun onCreate() {
        super.onCreate()
        Log.i("overlay", "overlay service started")

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        dialog = CustomLayout(this, null)


        dialog.cancel_button.setOnClickListener(this)


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

        Log.i("overlay", "view added")
        windowManager.addView(dialog, params)

        checkProximity()

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
                        if (isInProximity(latitude, longitude)) {
                            dialog.info_text.text = "Location proximity accepted"
                            Log.i("overlay", "location in proximity")
                        } else {
                            dialog.info_text.text = "Location proximity rejected"
                            dialog.title_text.text = "Payment Rejected"
                            dialog.card_text.text = ""
                            dialog.progress_circular.visibility = View.GONE
//                            dialog.cancel_button.visibility = View.GONE
                            Log.i("overlay", "location rejected")
                        }

                        Log.i(
                            "overlay",
                            "location: " + latitude.toString() + ", " + longitude.toString()
                        )
                    }
                },
                null
            )
        }

    }

    private fun checkPermission(): Boolean {
        Log.i("overlay", "checking location permissions")

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("overlay", "permission granted")
            return true
        }
        Log.i("location", "permission denied")
        return false
    }

    /* Calculates coordinates distance in meters and returns true or false depending on a set difference in meters*/
    fun isInProximity(latitude: Double, longitude: Double): Boolean {

        // Tempo coordinates: 57.706243 12.024906
        // Street coordinates: 57.705430 12.025805
        var zeroLat = 57.705430
        var zeroLong = 12.025805
        var earthRadius = 6378.137

        var dLat = latitude * Math.PI / 180 - zeroLat * Math.PI / 180
        var dLon = longitude * Math.PI / 180 - zeroLong * Math.PI / 180
        var a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(zeroLat * Math.PI / 180) * Math.cos(
                latitude * Math.PI / 180
            ) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distanceInMeters = earthRadius * c * 1000

        Log.i("overlay", "location in meters: " + Math.round(distanceInMeters))

        if (distanceInMeters <= 20.0) {
            Log.i("overlay", "in 20m proximity")
            return true
        } else {
            Log.i("overlay", "NOT in 20m proximity")
            return false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("overlay", "overlay service destroyed")
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.i("overlay", "overlay onBind")
        return null
    }

    override fun onClick(p0: View?) {
        Log.i("overlay", "overlay onClick")
        windowManager.removeView(dialog)
    }
}