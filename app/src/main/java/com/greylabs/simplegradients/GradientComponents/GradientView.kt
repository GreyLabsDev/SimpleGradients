package com.test.testgradient

import android.animation.TimeAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import com.greylabs.simplegradients.R
import java.util.*
import kotlin.collections.ArrayList

class GradientView : View {
    val TAG = "GradientView"
    enum class GradientOrientation {Vertical, Horizontal}
    
    private var middle: Float = 0f
    private var paintDrawable = PaintDrawable()
    private var rectShape = RectShape()
    private var endAnimationCallback: EndAnimationCallback? = null
    private var gradientColorsList: ArrayList<Int> = ArrayList()
    private var gradientPositionsList: ArrayList<FloatArray> = ArrayList()
    private var gradientAnimatiors: ArrayList<Animator> = ArrayList()
    private var colorsCount: Int = 5
    private var standardDuration: Long = 2000
    private var gradientOrientation = GradientOrientation.Horizontal
    private var loopAnimation = false
    
    constructor(context: Context) : super (context) {}
    
    constructor(context: Context,
                attributeSet: AttributeSet) : super (context, attributeSet) {initAttributes(attributeSet)}
    
    constructor(context: Context,
                attributeSet: AttributeSet,
                defStyleAttr: Int) : super (context, attributeSet, defStyleAttr) {initAttributes(attributeSet)}
    
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context,
                attributeSet: AttributeSet,
                defStyleAttr: Int,
                defStyleRes: Int) : super (context, attributeSet, defStyleAttr, defStyleRes) {initAttributes(attributeSet)}
    
    fun initAttributes(attributeSet: AttributeSet) {
        var typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.GradientView)
        var animationOnStart = typedArray.getBoolean(R.styleable.GradientView_animationOnStart, false)
        var animationLoop = typedArray.getBoolean(R.styleable.GradientView_animationLoop, false)
        var animationStepDuration = typedArray.getInt(R.styleable.GradientView_animationStepDuration, 2000).toLong()

        setStandartDuration(animationStepDuration)

        when (typedArray.getInt(R.styleable.GradientView_gradientOrientation, 0)) {
            0 -> gradientOrientation = GradientOrientation.Horizontal
            1 -> gradientOrientation = GradientOrientation.Vertical
        }

        gradientColorsList.clear()
        gradientColorsList.add(Color.parseColor("#FFFFFF"))
        gradientColorsList.add(Color.parseColor("#FFFFFF"))
        gradientColorsList.add(Color.parseColor("#FFFFFF"))


        Toast.makeText(context, "$gradientColorsList", Toast.LENGTH_LONG)

        Log.d("POSITIONS", " ${typedArray.getString(R.styleable.GradientView_startColorsPositions)}")
        Log.d("POSITIONS", " ${typedArray.getString(R.styleable.GradientView_endColorsPositions)}")

        typedArray.getString(R.styleable.GradientView_baseColors)?.let {
            gradientColorsList.clear()
            for (colorString in it.split(",")) {
                gradientColorsList.add(Color.parseColor(colorString))
            }
        }

        gradientPositionsList.clear()
        var startBasePositionsList: ArrayList<Float> = ArrayList()
        for (i in 0 until gradientColorsList.size) {
            startBasePositionsList.add(0.0f)
        }
        gradientPositionsList.add(startBasePositionsList.toFloatArray())

        typedArray.getString(R.styleable.GradientView_startColorsPositions)?.let {
            gradientPositionsList.clear()
            var startPositionsList: ArrayList<Float> = ArrayList()
            for (position in it.split(",")) {
                startPositionsList.add(position.toFloat())
            }
            gradientPositionsList.add(startPositionsList.toFloatArray())
        }

        typedArray.getString(R.styleable.GradientView_endColorsPositions)?.let{
            var endPositionsList: ArrayList<Float> = ArrayList()
            for (position in it.split(",")) {
                endPositionsList.add(position.toFloat())
            }
            gradientPositionsList.add(endPositionsList.toFloatArray())
        }

        typedArray.recycle()

        initGradientShader()

        if (gradientPositionsList.size >= 2) {
            initAnimationQueue()
            if (animationOnStart) {
                startAnimation()
            }
        }
    }
    
    fun setLoopAnimation(inValue : Boolean) {
        loopAnimation = inValue
    }

    fun setOrientation(inGradientOrientation: GradientOrientation) {
        gradientOrientation = inGradientOrientation
    }

    fun setStandartDuration(n: Long) {
        standardDuration = n
    }

    fun setEndCallBack(endCallBack: EndAnimationCallback) {
        endAnimationCallback = endCallBack
    }

    fun addGradientColor(inColor: String) {
        gradientColorsList.let {
            if (it.size < colorsCount) {
                it.add(Color.parseColor(inColor))
            } else {
                colorsCount++
                it.add(Color.parseColor(inColor))
            }
        }
    }
    
    fun addGradientPosition(vararg positions : Float) {
        var positionsList = ArrayList<Float>()
        for (pos in positions) {
            positionsList.add(pos)
        }
        gradientPositionsList.add(positionsList.toFloatArray())
        Log.d(TAG, "Pose added $positionsList")
    }
    
    fun initGradientShader() {
        Log.d(TAG, "gradientColorsList ${gradientColorsList?.size}")
        var shapeDrawableShader = object : ShapeDrawable.ShaderFactory() {
            override fun resize(p0: Int, p1: Int): Shader {
                var direction = floatArrayOf(0f, 0f)
                when (gradientOrientation) {
                    GradientOrientation.Horizontal -> {
                        direction[0] = width.toFloat()
                    }
                    GradientOrientation.Vertical -> {
                        direction[1] = height.toFloat()
                    }
                }
                return LinearGradient(0f, 0f,
                        direction[0], direction[1],
                        gradientColorsList?.toIntArray(), gradientPositionsList[0],
                        Shader.TileMode.CLAMP)
            }
        }
        paintDrawable.shape = rectShape
        paintDrawable.shaderFactory = shapeDrawableShader
        this.background = paintDrawable
    }
    
    fun initAnimationQueue() {
        if (gradientPositionsList.size == 2) {
            var grAnimator = Animator(paintDrawable, rectShape,
                gradientPositionsList[0], gradientPositionsList[1], standardDuration, gradientPositionsList[0])
            endAnimationCallback?.let {
                grAnimator.setEndCallback(it)
            }
            gradientAnimatiors.add(grAnimator)
        }
        if (gradientPositionsList.size >= 3) {
            for (i in 0 until gradientPositionsList.size - 1) {
                Log.d(TAG, "Pose idex $i, next pose ${i + 1}")
                var grAnimator = Animator(paintDrawable, rectShape,
                    gradientPositionsList[i], gradientPositionsList[i + 1], standardDuration,gradientPositionsList[0])
                gradientAnimatiors.add(grAnimator)
            }
            
            for (i in 0 until gradientAnimatiors.size - 1) {
                gradientAnimatiors[i].setNextAnimator(gradientAnimatiors[i + 1])
            }
            
            endAnimationCallback?.let{
                gradientAnimatiors[gradientAnimatiors.size - 1].setEndCallback(it)
            }
            Log.d(TAG, "Animation queue Size ${gradientAnimatiors.size}")
        }
        
    }
    
    fun startAnimation() {
        gradientAnimatiors[0].startAnimation()
    }
}

class Animator(paintDrawable: PaintDrawable, rectShape: RectShape,
               startPosition: FloatArray, endPosition: FloatArray,
               animationDuration: Long, initialPosition: FloatArray) {

    private val TAG = "GradientView.Animator"

    private var timeAnimator: TimeAnimator = TimeAnimator()
    private var nextAnimator: Animator? = null
    private var endCallBack: EndAnimationCallback? = null

    init {
        var positionsSteps: java.util.ArrayList<Float> = java.util.ArrayList()

        for (i in 0 until startPosition.size) {
            positionsSteps.add(((endPosition[i] - startPosition[i]) / animationDuration.toFloat()))
        }

        Log.d(TAG, "Animation init state steps = ${positionsSteps.toString()}")
        Log.d(TAG, "Animation init state start = ${Arrays.toString(startPosition)}")
        Log.d(TAG, "Animation init state toEnd = ${Arrays.toString(endPosition)}")

        timeAnimator.setTimeListener { animation, totalTime, deltaTime ->
            for (i in 0 until startPosition.size) {
                initialPosition[i] += positionsSteps[i] * deltaTime
            }
            if (totalTime < animationDuration) {
                paintDrawable.shape = rectShape
            } else {
                animation.cancel()
                if (endCallBack != null) {
                    endCallBack?.let {
                        Log.d(TAG, "End callback start")
                        it.startEndAnimation()
                    }
                } else {
                    nextAnimator?.let {
                        Log.d(TAG, "Next animation start")
                        Log.d(TAG, "Animation state end = ${Arrays.toString(startPosition)}")
                        it.startAnimation()
                    }
                }
            }
        }
    }

    fun startAnimation() {
        timeAnimator.start()
        Log.d(TAG, "Current animation start")
    }

    fun setEndCallback(inEndCallback: EndAnimationCallback) {
        endCallBack = inEndCallback
    }

    fun setNextAnimator(inAnimator: Animator) {
        nextAnimator = inAnimator
    }

    fun getTimeAnimator(): TimeAnimator? {
        return timeAnimator
    }

}

interface EndAnimationCallback {
    fun startEndAnimation()
}