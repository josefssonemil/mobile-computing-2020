package com.example.cardproximity

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import com.example.cardproximity.CustomLayout

class OverlayService : Service(), View.OnTouchListener, View.OnClickListener {

    private var moving = false
    private var initialTouchY = 0.0f
    private var initialTouchX = 0.0f
    private var initialY = 0
    private var initialX = 0
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager
    private lateinit var dialog: CustomLayout


    override fun onCreate() {
        super.onCreate()
        Log.i("overlay", "overlay service started")

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        dialog = CustomLayout(this, null)

        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START

        params.x = 0
        params.y = 3000

        windowManager.addView(dialog, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("overlay", "overlay service destroyed")
        windowManager.removeView(dialog)
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.i("overlay", "overlay onBind")
        return null
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        Log.i("overlay", "overlay onTouch")
        view!!.performClick()

//        when (event!!.action) {
//            MotionEvent.ACTION_DOWN -> {
//                initialX = params.x
//                initialY = params.y
//                initialTouchX = event.rawX
//                initialTouchY = event.rawY
//                moving = false
//            }
//            MotionEvent.ACTION_UP -> {
//                moving = false
//            }
//            MotionEvent.ACTION_MOVE -> {
//                params.x = initialX + (event.rawX - initialTouchX).toInt()
//                params.y = initialY + (event.rawY - initialTouchY).toInt()
//                windowManager.updateViewLayout(overlayButton, params)
//            }
//        }
        return true
    }

    override fun onClick(p0: View?) {
        Log.i("overlay", "overlay onClick")
    }
}