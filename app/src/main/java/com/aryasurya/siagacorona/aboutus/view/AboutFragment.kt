package com.aryasurya.siagacorona.aboutus.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.aryasurya.siagacorona.R
import kotlinx.android.synthetic.main.about_dialog.*


class AboutFragment: DialogFragment() {

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog!!.window?.setLayout(width, height)
        closeButton.setOnClickListener {
            dialog!!.cancel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_about_box)
        return inflater.inflate(R.layout.about_dialog, container, false)
    }
}