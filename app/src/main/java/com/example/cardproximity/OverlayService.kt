package com.example.cardproximity

import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.cardproximity.CustomLayout
import kotlinx.android.synthetic.main.custom_dialog_view_layout.view.*

class OverlayService : Service(), View.OnClickListener {

    private var moving = false
    private var initialTouchY = 0.0f
    private var initialTouchX = 0.0f
    private var initialY = 0
    private var initialX = 0
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager
    private lateinit var dialog: CustomLayout
    private lateinit var approvedDialog: ApprovedLayout
    private lateinit var abortedLayout: AbortedLayout
    private var i = 0
    private val handler = Handler()

    override fun onCreate() {

        super.onCreate()
        Toast.makeText(this, "overlay started", Toast.LENGTH_SHORT).show()
        Log.i("overlay", "overlay service started")

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        dialog = CustomLayout(this, null)
        approvedDialog = ApprovedLayout(this, null)
        abortedLayout = AbortedLayout(this, null)

        dialog.cancel_button.setOnClickListener(this)

        i = dialog.progress_circular!!.progress
        Thread(Runnable {

            while (i < 100) {
                i += 5
                if (i < 50) {
                    dialog.info_text!!.text = "Authenticating location..."
                } else {
                    dialog.info_text!!.text = "Authenticating proximity..."
                }

                try {
                    Thread.sleep(300)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            dialog.title_text.text = "Payment Approved"
            dialog.info_text.text = ""
            dialog.card_text.text = ""
            dialog.cancel_button.visibility = View.INVISIBLE
            dialog.progress_circular.visibility = View.INVISIBLE

        }).start()

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

        params.gravity = Gravity.TOP or Gravity.START

        params.x = 0
        params.y = 3000

        windowManager.addView(dialog, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "overlay destroyed", Toast.LENGTH_SHORT).show()
        Log.i("overlay", "overlay service destroyed")
        windowManager.removeView(dialog)
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.i("overlay", "overlay onBind")
        return null
    }

    override fun onClick(p0: View?) {
        Toast.makeText(this, "overlay clicked", Toast.LENGTH_SHORT).show()
        Log.i("overlay", "overlay onClick")
        onDestroy()
    }
}