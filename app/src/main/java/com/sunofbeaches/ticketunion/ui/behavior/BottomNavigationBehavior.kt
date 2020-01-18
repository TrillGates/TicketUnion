package com.sunofbeaches.ticketunion.ui.behavior

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

class BottomNavigationBehavior(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    //展示动画
    private var showAnimation: ValueAnimator? = null
    //隐藏动画
    private var hideAnimation: ValueAnimator? = null
    //状态,默认是显示的
    private var isNavigationBarShow = true

    companion object {
        const val DURATION = 200L
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray
    ) {
        //判断滑动方向
        if (dy > 0) {
            if (showAnimation == null) {
                showAnimation = ObjectAnimator.ofInt(0, -child.height)
                showAnimation!!.duration = DURATION
                val layoutPars: CoordinatorLayout.LayoutParams =
                    child.layoutParams as CoordinatorLayout.LayoutParams
                showAnimation!!.addUpdateListener {
                    layoutPars.setMargins(0, 0, 0, it.animatedValue as Int)
                    child.layoutParams = layoutPars
                }
            }
            //判断显示状态，如果此时是显示的，才去隐藏
            //隐藏
            if (isNavigationBarShow && showAnimation != null && !showAnimation?.isRunning!!) {
                showAnimation?.start()
                isNavigationBarShow = false
            }
        } else if (dy < 0) {
            if (hideAnimation == null) {
                hideAnimation = ObjectAnimator.ofInt(-child.height, 0)
                hideAnimation!!.duration = DURATION
                //显示
                val layoutPars: CoordinatorLayout.LayoutParams =
                    child.layoutParams as CoordinatorLayout.LayoutParams
                hideAnimation!!.addUpdateListener {
                    layoutPars.setMargins(0, 0, 0, it.animatedValue as Int)
                    child.layoutParams = layoutPars
                }
            }
            if (hideAnimation != null && !isNavigationBarShow && !hideAnimation?.isRunning!!) {
                hideAnimation?.start()
                isNavigationBarShow = true
            }
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
}