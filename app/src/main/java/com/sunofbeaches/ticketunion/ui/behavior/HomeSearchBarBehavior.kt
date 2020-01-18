package com.sunofbeaches.ticketunion.ui.behavior

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.sunofbeaches.ticketunion.R

class HomeSearchBarBehavior(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    //展示动画
    private var showAnimation: ValueAnimator? = null
    //隐藏动画
    private var hideAnimation: ValueAnimator? = null
    //状态,默认是显示的
    private var isNavigationBarShow = true
    //
    private var targetChild: View? = null

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
        //TODO:优化，求dx的角度，如果是横向的话，不隐藏，直接return.
       // LogUtils.d(this, "dy === > $dy")
        if (targetChild == null) {
            targetChild = child.findViewById<RelativeLayout>(R.id.home_search_bar)
        }

        if (targetChild == null) {
            //其他页面就没必要执行下面的代码了
            return
        }
        //判断滑动方向
        if (dy > 0) {
            if (showAnimation == null) {
                showAnimation = ObjectAnimator.ofInt(0, -targetChild?.height!!)
                showAnimation!!.duration = DURATION
                val layoutPars: LinearLayout.LayoutParams =
                    targetChild?.layoutParams as LinearLayout.LayoutParams
                showAnimation!!.addUpdateListener {
                    val value = it.animatedValue as Int
                    layoutPars.setMargins(0, value, 0, 0)
                    targetChild?.layoutParams = layoutPars
                    if (value == -targetChild?.height!!) {
                        isNavigationBarShow = false
                    }
                }
            }
            //判断显示状态，如果此时是显示的，才去隐藏
            //隐藏
            if (isNavigationBarShow && showAnimation != null && !showAnimation?.isRunning!!) {
                showAnimation?.start()
            }
        } else if (dy < 0) {
            if (hideAnimation == null) {
                hideAnimation = ObjectAnimator.ofInt(-targetChild?.height!!, 0)
                hideAnimation!!.duration = DURATION
                //显示
                val layoutPars: LinearLayout.LayoutParams =
                    targetChild?.layoutParams as LinearLayout.LayoutParams
                hideAnimation!!.addUpdateListener {
                    val value = it.animatedValue as Int
                    layoutPars.setMargins(0, value, 0, 0)
                    targetChild?.layoutParams = layoutPars
                    if (value == 0) {
                        isNavigationBarShow = true
                    }
                }
            }
            if (hideAnimation != null && !isNavigationBarShow && !hideAnimation?.isRunning!!) {
                hideAnimation?.start()
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