package com.example.cardproximity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.example.cardproximity.sound.SoundHandler
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


       var soundHandler : SoundHandler = SoundHandler(this)


        // Should play after proximity thing

        soundHandler.startPlay()
        soundHandler.startListening()

        get_position_button.setOnClickListener {
            val intent = Intent(this, LocationService::class.java)
            startActivity(intent)

        }

        // Overlay Service starts here
        var canDraw = true

        var intent: Intent? = null

        intent = Intent(this, LocationService::class.java)
        startActivity(intent)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            canDraw = Settings.canDrawOverlays(this)
            if (!canDraw && intent != null) {
                startActivity(intent)
            }
        }


        var service = Intent(this, OverlayService::class.java)
        startService(service)

        start_service.setOnClickListener {
            service = Intent(this, OverlayService::class.java)
            startService(service)
        }


//        stop_service.setOnClickListener {
//            val service = Intent(this, OverlayService::class.java)
//            stopService(service)
//        }
    }


}