package com.example.cardproximity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class LocationService : Service() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    private val PERMISSION_ID = 1010
    private lateinit var mainActivity: MainActivity

    override fun onCreate() {
        super.onCreate()



//        requestPermission()
        getLastLocation()
    }

    // location service starts here
    fun getLastLocation() {
        Log.i("location", "getLastLocation()")
        if (checkPermission()) {

            if (true) {

                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        getNewLocation()
                    } else {
                        Log.i(
                            "position",
                            "location: " + location.latitude + " " + location.longitude
                        )
                    }
                }
            } else {
                Log.i("location", "location is turned off")
            }

        } else {
//            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    fun getNewLocation() {
        Log.i("location", "getNewLocation()")
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val lastLocation: Location = locationResult.lastLocation
            Log.i(
                "position",
                "last location: " + lastLocation.latitude.toString() + " " + lastLocation.longitude.toString()
            )
        }
    }

    fun checkPermission(): Boolean {
        Log.i("location", "checkPermission()")

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("location", "checkSelfPermission() == true")
            return true
        }
        Log.i("location", "checkSelfPermission() == false")
        return false
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            mainActivity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

//    fun isLocationEnabled(): Boolean {
//        val locationManager = getSystemService(mainActivity.applicationContext.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//            LocationManager.NETWORK_PROVIDER
//        )
//    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Log.i("position", "permission is granted")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}



