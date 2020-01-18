package com.sunofbeaches.ticketunion.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyBoardUtils {

    companion object{
        fun hideKeyboard(context: Context,view: View) {
            val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)
        }


        fun showKeyboard(context: Context,view: View) {
            val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view,0)
        }
    }
}