package com.greylabs.simplegradients

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.test.testgradient.EndAnimationCallback
import com.test.testgradient.GradientView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        initTitleBgGradient(this)
        initLeftBgGradient(this)
        initRightBgGradient(this)
        initRightBgGradientTwo(this)
    }

    fun initTitleBgGradient(inContext: Context) {
        var gradientView = GradientView(inContext)

        gradientView.apply {
            setOrientation(GradientView.GradientOrientation.Horizontal)
            addGradientColor("#04006E")
            addGradientColor("#19FC90")
            
            addGradientPosition(0f, 1f)
            initGradientShader()
            clUpContainer.addView(gradientView, 0)
        }

    }

    fun initLeftBgGradient(inContext: Context) {
        var gradientView = GradientView(inContext)
        gradientView.apply {
            setOrientation(GradientView.GradientOrientation.Vertical)
            addGradientColor("#09B2D1")
            addGradientColor("#A02CFF")

            addGradientColor("#2622FB")
            addGradientColor("#19FC90")
    
            addGradientPosition(0f, 1f, 1.1f, 1.2f)
            addGradientPosition(0f, 0f, 0f, 1f)
            addGradientPosition(0f, 0.21f, 0.4f, 0.6f)
            addGradientPosition(0f, 0.25f, 0.5f, 0.75f)

            initGradientShader()

            setStandartDuration(2000L)
            initAnimationQueue()
            setEndCallBack(object : EndAnimationCallback {
                override fun startEndAnimation() {
                    gradientView.startAnimation()
                }
            })

            clDownLeftContainer.addView(gradientView, 0)

            startAnimation()
        }
    }

    fun initRightBgGradient(inContext: Context) {
        var gradientView = GradientView(inContext)
        gradientView.apply {
            setOrientation(GradientView.GradientOrientation.Horizontal)
            addGradientColor("#2622FB")
            addGradientColor("#19FC90")

            addGradientColor("#09B2D1")
            addGradientColor("#A02CFF")
    
            addGradientPosition(0f, 1f, 1.1f, 1.2f)
            addGradientPosition(0f, 0f, 0f, 1f)
            addGradientPosition(0f, 0.21f, 0.4f, 0.6f)
            addGradientPosition(0f, 0.25f, 0.5f, 0.75f)

            initGradientShader()

            setStandartDuration(4000L)
            initAnimationQueue()
            setEndCallBack(object : EndAnimationCallback {
                override fun startEndAnimation() {
                    gradientView.startAnimation()
                }
            })

            clDownRightContainer.addView(gradientView, 0)

            startAnimation()
        }
    }

    fun initRightBgGradientTwo(inContext: Context) {
        var gradientView = GradientView(inContext)
        gradientView.apply {
            setOrientation(GradientView.GradientOrientation.Vertical)
            addGradientColor("#0B01D1")
            addGradientColor("#E5008E")

            addGradientColor("#A02CFF")
            addGradientColor("#19FC90")
    
            addGradientPosition(0f, 1f, 1.1f, 1.2f)
            addGradientPosition(0f, 0f, 0f, 1f)
            addGradientPosition(0f, 0.21f, 0.4f, 0.6f)
            addGradientPosition(0f, 0.25f, 0.5f, 0.75f)
            
            initGradientShader()

            setStandartDuration(8000L)
            initAnimationQueue()
            setEndCallBack(object : EndAnimationCallback {
                override fun startEndAnimation() {
                    gradientView.startAnimation()
                }
            })

            clDownRightContainerTwo.addView(gradientView, 0)

            startAnimation()
        }
    }
}
