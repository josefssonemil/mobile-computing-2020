package com.example.locationbasedcardproximity

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.media.Image
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.w3c.dom.Text

class OverlayService : Service(), View.OnTouchListener, View.OnClickListener {


    private var moving = false
    private var initialTouchY = 0.0f
    private var initialTouchX = 0.0f
    private var initialY = 0
    private var initialX = 0
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager
    private lateinit var overlayButton: ImageButton
    private lateinit var modalDialog: BottomSheetDialog
    private lateinit var layout: LinearLayout

    private lateinit var fragmentManager: FragmentManager

    override fun onCreate() {
        super.onCreate()

        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

//        overlayButton = ImageButton(this)
//        overlayButton.setImageResource(R.drawable.test)

//        overlayButton.setOnTouchListener(this)
//        overlayButton.setOnClickListener(this)

        layout = LinearLayout(this)
        layout.setBackgroundColor(Color.WHITE)

        val testText = TextView(this)
        testText.textSize = 20f
        testText.text = "Testing"
        testText.width = 1080

        val testImage = ImageView(this)
        testImage.setImageResource(R.drawable.ic_launcher_foreground)

        val testButton = Button(this)
        testButton.text = "Test me"

        layout.addView(testText)
        layout.addView(testButton)
        layout.addView(testImage)


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
        params.y = 2160

        windowManager.addView(layout, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayButton)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
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
        if (!moving) Toast.makeText(this, "Button touched", Toast.LENGTH_SHORT).show()
    }
}