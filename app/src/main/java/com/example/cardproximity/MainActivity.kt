package com.example.cardproximity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings

import androidx.lifecycle.ViewModelProvider
import com.example.cardproximity.sound.SoundHandler
import com.example.cardproximity.viewModels.PaymentViewModel


class MainActivity : AppCompatActivity() {



    private lateinit var viewModel: PaymentViewModel





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)





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

        // To simulate app starting minimized
        var i: Intent = Intent(Intent.ACTION_MAIN)
        i.addCategory(Intent.CATEGORY_HOME)
        startActivity(i)
    }
}