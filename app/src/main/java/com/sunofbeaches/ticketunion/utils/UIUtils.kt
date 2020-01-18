package com.sunofbeaches.ticketunion.utils

import android.widget.Toast
import com.sunofbeaches.ticketunion.base.BaseApplication

object UIUtils {

    private val toast: Toast = Toast.makeText(BaseApplication.appContext, "", Toast.LENGTH_LONG)
    fun toast(text: String) {
        toast.setText(text)
        toast.show()
    }

}