package com.sunofbeaches.ticketunion.ui.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import com.sunofbeaches.ticketunion.utils.LogUtils

class LoadingView(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
    AppCompatImageView(context, attrs, defStyleAttr) {


    init {
        LogUtils.d(this, "LoadingView init....")
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs!!, 0)

    constructor(context: Context) : this(context, null)

    private var needRotating = false
    private var rotateDegree = 0.0f

    override fun onAttachedToWindow() {
        LogUtils.d(this, "onAttachedToWindow...")
        super.onAttachedToWindow()
        startRotate()
    }

    fun startRotate() {
        needRotating = true
        post(object : Runnable {
            override fun run() {
                //  LogUtils.d(TAG, "rotating...")
                rotateDegree += 10.0f
                //范围判断，如果超过了360度，则变成0度
                rotateDegree = if (rotateDegree <= 360) rotateDegree else 0.0f
                invalidate()
                //判断是否再旋转
                if (needRotating && isVisible) {
                    removeCallbacks(this)
                    postDelayed(this, 20)
                }
            }
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        LogUtils.d(this, "onDetachedFromWindow...")
        stopRotate()
    }

    fun stopRotate() {
        needRotating = false
    }

    override fun onDraw(canvas: Canvas?) {
        // LogUtils.d(TAG, "rotateDegree -- > $rotateDegree")
        canvas?.rotate(rotateDegree, (width / 2).toFloat(), (height / 2).toFloat())
        super.onDraw(canvas)
    }

}