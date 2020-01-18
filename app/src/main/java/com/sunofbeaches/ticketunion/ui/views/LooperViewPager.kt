package com.sunofbeaches.ticketunion.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.sunofbeaches.ticketunion.utils.LogUtils

class LooperViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    constructor(context: Context) : this(context, null)

    private val loopFrequent = 3000L

    private val task: Runnable = object : Runnable {
        override fun run() {
            currentItem++
            postDelayed(this, loopFrequent)
        }

    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                removeCallbacks(task)
            }
            MotionEvent.ACTION_UP -> {
                postDelayed(task, loopFrequent)
            }
            MotionEvent.ACTION_CANCEL -> {
                postDelayed(task, loopFrequent)
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        LogUtils.d(this, "onAttachedToWindow")
        post(task)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        LogUtils.d(this, "onAttachedToWindow")
        removeCallbacks(task)
    }

}