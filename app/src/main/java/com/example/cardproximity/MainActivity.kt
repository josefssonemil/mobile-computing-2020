package com.example.locationbasedcardproximity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnBottomSheetModal.setOnClickListener {
            CustomBottomSheetDialogFragment().apply {
                show(supportFragmentManager, CustomBottomSheetDialogFragment.TAG)
            }
        }

        var canDraw = true

        var intent: Intent? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            canDraw = Settings.canDrawOverlays(this)
            if (!canDraw && intent != null) {
                startActivity(intent)
            }
        }


        val service = Intent(this, OverlayService::class.java)
        startService(service)

        start_service.setOnClickListener {
            val service = Intent(this, OverlayService::class.java)
            startService(service)
        }

        stop_service.setOnClickListener {
            val service = Intent(this, OverlayService::class.java)
            stopService(service)
        }
    }

}