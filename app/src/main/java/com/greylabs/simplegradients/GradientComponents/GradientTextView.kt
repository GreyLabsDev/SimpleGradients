package com.test.testgradient

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.greylabs.simplegradients.R
import java.util.*
import kotlin.collections.ArrayList

open class GradientTextView : TextView {

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
	
	private fun initAttributes(attributeSet: AttributeSet) {
		var typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.GradientTextView)
		var direction = floatArrayOf(0f, 0f)

		var baseColors : ArrayList<Int> = ArrayList()
		baseColors.add(typedArray.getColor(R.styleable.GradientTextView_baseColorOne, Color.parseColor("#40E0D0")))
		baseColors.add(typedArray.getColor(R.styleable.GradientTextView_baseColorTwo, Color.parseColor("#56CCF2")))
		baseColors.add(typedArray.getColor(R.styleable.GradientTextView_baseColorThree, Color.parseColor("#FFFFFF")))

		this.post {
			when (typedArray.getInt(R.styleable.GradientTextView_gradientOrientation, 0)) {
				0 -> direction[0] = this.width.toFloat()
				1 -> direction[1] = this.height.toFloat()
			}
			Log.d("GradientTextView", Arrays.toString(direction))
			var shader = LinearGradient(0f, 0f, direction[0], direction[1],
					baseColors.toIntArray(),null, Shader.TileMode.CLAMP)
			this.paint.shader = shader
			typedArray.recycle()
			this.invalidate()
		}
	}
	
}