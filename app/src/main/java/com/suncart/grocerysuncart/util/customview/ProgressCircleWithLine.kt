package com.suncart.grocerysuncart.util.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.transition.CircularPropagation
import android.util.AttributeSet
import android.view.View
import com.suncart.grocerysuncart.R
import java.util.jar.Attributes

class ProgressCircleWithLine(context: Context?, attributes: AttributeSet?) : View(context, attributes){

    private val paintCicle = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintLine = Paint(Paint.ANTI_ALIAS_FLAG)

    companion object{
        const val CIRCLE_COLOR = "#a4a5a6"
        const val LINE_COLOR = "#a4a5a6"

        const val CIRCLE_IN_BETWEEN = false
        const val CIRCLE_IN_BOTTOM = false
        const val CIRCLE_IN_TOP = false
    }

    var isCircleInBetween : Boolean = CIRCLE_IN_BETWEEN
    var isCircleInBottom : Boolean = CIRCLE_IN_BOTTOM
    var isCircleInTop : Boolean = CIRCLE_IN_TOP

    var circleInTopColor = CIRCLE_COLOR
    var circleInTopLineColor = CIRCLE_COLOR

    var circleInBetweenColor = CIRCLE_COLOR
    var circleInBetweenLineColor = CIRCLE_COLOR

    var circleInBottomColor = CIRCLE_COLOR
    var circleInBottomLineColor = CIRCLE_COLOR

    var circleLineColor = CIRCLE_COLOR
    set(value){
        field = value
        invalidate()
    }

    var lineColor = LINE_COLOR
    set(value) {
        field = value
        invalidate()
    }

    init {
        paintCicle.isAntiAlias = true
        paintLine.isAntiAlias = true
        setupAttributes(attributes)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCircle(canvas!!)
    }

    private fun drawCircle(canvas: Canvas){
        if (isCircleInTop){
            paintCicle.color = Color.parseColor(circleInTopColor)
            paintCicle.strokeWidth = 3f
            paintCicle.style = Paint.Style.FILL_AND_STROKE

            paintLine.color = Color.parseColor(circleInTopLineColor)
            paintLine.strokeWidth = 3f
            paintLine.style = Paint.Style.STROKE
            paintLine.strokeCap = Paint.Cap.SQUARE

            val mWidth = width / 2
            val mHeight = height / 2
            canvas.drawCircle(mWidth.toFloat(), 35f, (mWidth / 2).toFloat(), paintCicle)
            canvas.drawLine((mWidth).toFloat(), 18f,
                (mWidth).toFloat(), height.toFloat(), paintLine)
        }else if (isCircleInBetween){
            paintCicle.color = Color.parseColor(circleInBetweenColor)
            paintCicle.strokeWidth = 3f
            paintCicle.style = Paint.Style.FILL_AND_STROKE

            paintLine.color = Color.parseColor(circleInBetweenLineColor)
            paintLine.strokeWidth = 3f
            paintLine.style = Paint.Style.STROKE
            paintLine.strokeCap = Paint.Cap.SQUARE

            val mWidth = width / 2
            val mHeight = height / 2
            canvas.drawCircle(mWidth.toFloat(),
                (mHeight).toFloat(), (mWidth / 2).toFloat(), paintCicle)
            canvas.drawLine((mWidth).toFloat(), 0f,
                (mWidth).toFloat(), height.toFloat(), paintLine)
        }else if (isCircleInBottom){
            paintCicle.color = Color.parseColor(circleInBottomColor)
            paintCicle.strokeWidth = 3f
            paintCicle.style = Paint.Style.FILL_AND_STROKE

            paintLine.color = Color.parseColor(circleInBottomLineColor)
            paintLine.strokeWidth = 3f
            paintLine.style = Paint.Style.STROKE
            paintLine.strokeCap = Paint.Cap.SQUARE

            val mWidth = width / 2
            val mHeight = height / 2
            canvas.drawCircle(mWidth.toFloat(), mHeight.toFloat(), (mWidth / 2).toFloat(), paintCicle)
            canvas.drawLine((mWidth).toFloat(), 0f,
                (mWidth).toFloat(), height.toFloat() / 2, paintLine)
        }

    }

    private fun setupAttributes(attrs: AttributeSet?) {
        // 6
        // Obtain a typed array of attributes
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CircleProgressLine,
            0, 0)
        isCircleInTop = typedArray.getBoolean(R.styleable.CircleProgressLine_circleOnTop,false)
        isCircleInBetween = typedArray.getBoolean(R.styleable.CircleProgressLine_circleOnBetween,false)
        isCircleInBottom = typedArray.getBoolean(R.styleable.CircleProgressLine_circleOnBottom,false)

        typedArray.recycle()
    }
}