package com.example.cardproximity

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.custom_dialog_view_layout.view.*

class OverlayService : Service(), View.OnClickListener {

    private lateinit var params: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager

    private lateinit var dialog: CustomLayout
    private lateinit var approvedDialog: ApprovedLayout
    private lateinit var abortedLayout: AbortedLayout
    private var i = 0

    override fun onCreate() {

        super.onCreate()
        Log.i("overlay", "overlay service started")

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        dialog = CustomLayout(this, null)
        approvedDialog = ApprovedLayout(this, null)
        abortedLayout = AbortedLayout(this, null)

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
        Log.i("overlay", "Thread sleeping")
//        Thread.sleep(3000)
        Log.i("overlay", "view updated")
//        windowManager.removeView(dialog)
//        windowManager.addView(approvedDialog, params)

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("overlay", "overlay service destroyed")
        //windowManager.removeView(dialog)
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.i("overlay", "overlay onBind")
        return null
    }

    override fun onClick(p0: View?) {
        Log.i("overlay", "overlay onClick")
        windowManager.removeView(dialog)
        windowManager.addView(abortedLayout, params)
    }
}