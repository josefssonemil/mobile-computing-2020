package com.example.cardproximity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings

import com.example.cardproximity.sound.SoundHandler


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var soundHandler: SoundHandler = SoundHandler(this)

        soundHandler.start()

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

        var service = Intent(this, OverlayService::class.java)
        startService(service)

    }
}