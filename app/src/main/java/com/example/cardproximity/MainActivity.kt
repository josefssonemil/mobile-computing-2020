package com.example.cardproximity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Overlay Service starts here
        var canDraw = true
        var intent: Intent? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            canDraw = Settings.canDrawOverlays(this)
            if (!canDraw && intent != null) {
                startActivity(intent)
            }
        }

//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.RECORD_AUDIO
//            )
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this, arrayOf(Manifest.permission.RECORD_AUDIO),
//                1234
//            )
//        }

        var service = Intent(this, OverlayService::class.java)
        startService(service)

        // To simulate app starting minimized
        var i: Intent = Intent(Intent.ACTION_MAIN)
        i.addCategory(Intent.CATEGORY_HOME)
        startActivity(i)
    }
}