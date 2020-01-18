package com.sunofbeaches.ticketunion.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sunofbeaches.ticketunion.R


class FlowTextLayout(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
    ViewGroup(context, attrs, defStyleAttr) {


    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private var mItemClickListener: ItemClickListener? = null

    fun setTextContents(texts: List<String>) {
        contentList.clear()
        contentList.addAll(texts)
        this.removeAllViews()
        for (text in contentList) {
            val item =
                LayoutInflater.from(context).inflate(R.layout.item_text, this, false) as TextView
            item.text = text
            item.setOnClickListener {
                mItemClickListener?.onItemClick(text)
            }
            addView(item)
        }

    }

    fun setClickListener(listener: ItemClickListener) {
        this.mItemClickListener = listener
    }

    interface ItemClickListener {
        fun onItemClick(text: String)
    }

    private var mHorizontalSpace: Int = 20
    private var mVerticalSpace: Int = 20

    fun setSpace(horizontalSpace: Int, verticalSpace: Int) {
        this.mHorizontalSpace = horizontalSpace
        this.mVerticalSpace = verticalSpace
    }

    var contentList: ArrayList<String> = ArrayList()


    private val mLines: ArrayList<Line> = ArrayList()


    private var mCurrentLineCursor: Line? = null

    /**
     * 测量分两步，先测量孩子，再测量自己，关于自定义控件
     * 我们会在自定义控件的课程里详细给大家讲解onMeasure，onLayout方法。自定义ViewGroup的步骤。
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mLines.clear()
        mCurrentLineCursor = null
        //获取宽度
        val layoutWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        //计算最大宽度
        val maxLineWidth = layoutWidth - paddingLeft - paddingRight
        //先测量孩子
        val count = childCount
        for (i in 0 until count) {
            val view = getChildAt(i)
            // 如果孩子不可见
            if (view.visibility === View.GONE) {
                continue
            }

            // 测量孩子
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
            // 往lines添加孩子
            if (mCurrentLineCursor == null) {
                // 说明还没有开始添加孩子
                mCurrentLineCursor = Line(maxLineWidth, mHorizontalSpace)
                // 添加到 Lines中
                mLines.add(mCurrentLineCursor!!)
                // 行中一个孩子都没有
                mCurrentLineCursor?.addView(view)
            } else {
                // 行不为空,行中有孩子了
                val canAdd = mCurrentLineCursor?.canAdd(view)
                if (canAdd!!) {
                    // 可以添加
                    mCurrentLineCursor?.addView(view)
                } else {
                    // 新建行
                    mCurrentLineCursor = Line(maxLineWidth, mHorizontalSpace)
                    // 添加到lines中
                    mLines.add(mCurrentLineCursor!!)
                    // 将view添加到line
                    mCurrentLineCursor?.addView(view)
                }
            }
        }

        // 设置自己的宽度和高度
        var allHeight = 0f
        for (i in 0 until mLines.size) {
            val mHeigth = mLines[i].height
            // 加行高
            allHeight += mHeigth
            // 加间距
            if (i != 0) {
                allHeight += mVerticalSpace
            }
        }

        val measuredHeight =
            (allHeight + paddingTop.toFloat() + paddingBottom.toFloat() + 0.5f).toInt()
        setMeasuredDimension(layoutWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 给Child 布局---> 给Line布局
        val paddingLeft = paddingLeft
        var offsetTop = paddingTop
        for (i in 0 until mLines.size) {
            val line = mLines.get(i)
            // 给行布局
            line.layout(paddingLeft, offsetTop)
            offsetTop += line.height + mVerticalSpace
        }
    }

    inner class Line(maxWidth: Int, horizontalSpace: Int) {
        // 属性
        val mViews = ArrayList<View>()
        val mMaxWidth: Int = maxWidth
        var mUsedWidth = 0
        var height = 0
        val mMarginLeft = 0
        val mMarginRight = 0
        val mMarginTop = 0
        val mMarginBottom = 0
        val mHorizontalSpace = horizontalSpace

        /**
         * 添加view，记录属性的变化
         *
         * @param view
         */
        fun addView(view: View) {
            // 加载View的方法
            val size = mViews.size
            val viewWidth = view.measuredWidth
            val viewHeight = view.measuredHeight
            // 计算宽和高
            if (size == 0) {
                // 说还没有添加View
                if (viewWidth > mMaxWidth) {
                    mUsedWidth = mMaxWidth
                } else {
                    mUsedWidth = viewWidth
                }
                height = viewHeight
            } else {
                // 多个view的情况
                mUsedWidth += viewWidth + mHorizontalSpace
                height = if (height < viewHeight) viewHeight else height
            }
            // 将View记录到集合中
            mViews.add(view)
        }

        /**
         * 用来判断是否可以将View添加到line中
         *
         * @param view
         * @return
         */
        fun canAdd(view: View): Boolean {
            // 判断是否能添加View
            val size = mViews.size
            if (size == 0) {
                return true
            }
            val viewWidth = view.measuredWidth
            // 预计使用的宽度
            val planWidth = mUsedWidth + mHorizontalSpace + viewWidth.toFloat()
            return planWidth <= mMaxWidth
        }

        /**
         * 给孩子布局
         *
         * @param offsetLeft
         * @param offsetTop
         */
        fun layout(offsetLeft: Int, offsetTop: Int) {
            // 给孩子布局
            var currentLeft = offsetLeft
            val size = mViews.size
            // 判断已经使用的宽度是否小于最大的宽度
            var extra = 0
            var widthAvg = 0
            if (mMaxWidth > mUsedWidth) {
                extra = mMaxWidth - mUsedWidth
                widthAvg = extra / size
            }

            for (i in 0 until size) {
                val view = mViews[i]
                var viewWidth = view.measuredWidth
                var viewHeight = view.measuredHeight
                // 判断是否有富余
                if (widthAvg != 0) {
                    // 改变宽度
                    val newWidth = (viewWidth.toFloat() + widthAvg + 0.5f).toInt()
                    val widthMeasureSpec =
                        MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.AT_MOST)
                    val heightMeasureSpec =
                        MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
                    view.measure(widthMeasureSpec, heightMeasureSpec)

                    viewWidth = view.measuredWidth
                    viewHeight = view.measuredHeight
                }

                // 布局
                val left = currentLeft
                val top = (offsetTop.toFloat() + (height - viewHeight) / 2 + 0.5f).toInt()
                // int top = offsetTop;
                val right = left + viewWidth
                //Log.d(TAG, "viewWidth -- > $viewWidth")
                val bottom = top + viewHeight
                view.layout(left, top, right, bottom)
                currentLeft += (viewWidth + mHorizontalSpace)
            }
        }
    }
}