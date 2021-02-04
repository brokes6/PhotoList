package com.laboratory.gallery

import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator


class CircleProgressBar : View {
    private var mContext: Context
    private lateinit var mPaint: Paint
    private var mProgress = 0

    /**
     * 弧度
     */
    private var mAngle = 0F

    /**
     * 中间的文字
     */
    private var mText: String? = null

    /**
     * 外圆颜色
     */
    private var outRoundColor = 0

    /**
     * 内圆的颜色
     */
    private var inRoundColor = 0

    /**
     * 线的宽度
     */
    private var roundWidth = 0F
    private val style = 0

    /**
     * 字体颜色
     */
    private var textColor = 0

    /**
     * 字体大小
     */
    private var textSize = 0f

    /**
     * 字体是否加粗
     */
    private var isBold = false

    /**
     * 进度条颜色
     */
    private var progressBarColor = 0

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        init(attrs)
    }

    @TargetApi(21)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        mContext = context
        init(attrs)
    }

    /**
     * 解析自定义属性
     *
     * @param attrs
     */
    private fun init(attrs: AttributeSet?) {
        mPaint = Paint()
        val typedArray: TypedArray =
            mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar)
        outRoundColor = typedArray.getColor(
            R.styleable.CircleProgressBar_outCircleColor,
            resources.getColor(R.color.colorPrimary, null)
        )
        inRoundColor = typedArray.getColor(
            R.styleable.CircleProgressBar_inCircleColor,
            resources.getColor(R.color.colorPrimaryDark, null)
        )
        progressBarColor = typedArray.getColor(
            R.styleable.CircleProgressBar_progressColor,
            resources.getColor(R.color.colorAccent, null)
        )
        isBold = typedArray.getBoolean(R.styleable.CircleProgressBar_textBold, false)
        textColor = typedArray.getColor(R.styleable.CircleProgressBar_textColor, Color.BLACK)
        roundWidth =
            typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBar_lineWidth, 20)
                .toFloat()
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        /**
         * 画外圆
         */
        super.onDraw(canvas)
        val center = width.toFloat() / 2 //圆心
        var radius = center - roundWidth / 2 //半径
        mPaint.color = outRoundColor //外圆颜色
        mPaint.strokeWidth = roundWidth //线的宽度
        mPaint.style = Paint.Style.STROKE //空心圆
        mPaint.isAntiAlias = true //消除锯齿
        canvas.drawCircle(center, center, radius, mPaint)
        //内圆
        mPaint.color = inRoundColor
        //这里会导致加载进度的园半径变小
//        radius -= roundWidth
        canvas.drawCircle(center, center, radius, mPaint)

        //画进度是一个弧线
        mPaint.color = progressBarColor
        val rectF = RectF(
            (center - radius),
            (center - radius), (center + radius), (center + radius)
        ) //圆弧范围的外接矩形
        canvas.drawArc(rectF, -90F, mAngle, false, mPaint)
        canvas.save() //平移画布之前保存之前画的

        //画进度终点的小球,旋转画布的方式实现
        mPaint.style = Paint.Style.FILL
        //将画布坐标原点移动至圆心
        canvas.translate(center, center)
        //旋转和进度相同的角度，因为进度是从-90度开始的所以-90度
        canvas.rotate((mAngle - 90))
        //同理从圆心出发直接将原点平移至要画小球的位置
        canvas.translate(radius, 0F)
        canvas.drawCircle(0F, 0F, roundWidth, mPaint)
        //画完之后恢复画布坐标
        canvas.restore()

        //画文字将坐标平移至圆心
        canvas.translate(center, center)
        mPaint.strokeWidth = 0F
        mPaint.color = textColor
        if (isBold) {
            //字体加粗
            mPaint.typeface = Typeface.DEFAULT_BOLD
        }
        if (TextUtils.isEmpty(mText)) {
            mText = "$mProgress%"
        }
        //动态设置文字长为圆半径，计算字体大小
        val textLength = mText!!.length.toFloat()
        textSize = radius / textLength
        mPaint.textSize = textSize
        //将文字画到中间
        val textWidth: Float = mPaint.measureText(mText)
        canvas.drawText(mText!!, -textWidth / 2, textSize / 2, mPaint)
    }

    fun getmProgress(): Int {
        return mProgress
    }

    /**
     * 设置进度
     *
     * @return
     */
    fun setmProgress(p: Int) {
        if (p > MAX_PROGRESS) {
            mProgress = MAX_PROGRESS
            mAngle = 360F
        } else {
            mProgress = p
            mAngle = (360 * p / MAX_PROGRESS).toFloat()
        }
    }

    fun getmText(): String? {
        return mText
    }

    /**
     * 设置文本
     *
     * @param mText
     */
    fun setmText(mText: String?) {
        this.mText = mText
    }

    /**
     * 设置带动画的进度
     * @param p
     */
    fun setAnimProgress(p: Int) {
        mProgress = if (p > MAX_PROGRESS) {
            MAX_PROGRESS
        } else {
            p
        }
        //设置属性动画
        val valueAnimator = ValueAnimator.ofInt(getmProgress(), p)
        //动画从快到慢
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = 3000
        //监听值的变化
        valueAnimator.addUpdateListener { animation ->
            val currentV = animation.animatedValue as Int
            mAngle = (360 * currentV / MAX_PROGRESS).toFloat()
            mText = "$currentV%"
            invalidate()
        }
        valueAnimator.start()
    }

    companion object {
        private const val MAX_PROGRESS = 100
    }
}