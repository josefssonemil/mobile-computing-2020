package com.example.cardproximity

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class CustomLayout(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var titleText: TextView
    private var infoText: TextView
    private var cancelButton: Button

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_dialog_view_layout, this, true)
        titleText = view.findViewById(R.id.title_text)
        infoText = view.findViewById(R.id.info_text)
        cancelButton = view.findViewById(R.id.cancel_button)
    }
}