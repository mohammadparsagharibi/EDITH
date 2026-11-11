
package com.parnix.edith

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.animation.ValueAnimator
import android.widget.*

class MainActivity : Activity() {

    private val bg = Color.rgb(3, 9, 22)
    private val panel = Color.rgb(9, 23, 43)
    private val cyan = Color.rgb(70, 220, 255)
    private val white = Color.WHITE

    private lateinit var root: LinearLayout
    private lateinit var statusText: TextView

    private fun dp(v: Int) =
        (v * resources.displayMetrics.density).toInt()

    private fun text(
        value: String,
        size: Float,
        color: Int,
        bold: Boolean = false
    ): TextView {
        return TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER
            if (bold) setTypeface(null, android.graphics.Typeface.BOLD)
        }
    }

    private fun button(value: String): Button {
        return Button(this).apply {
            text = value
            textSize = 13f
            isAllCaps = false
            setTextColor(white)
            background = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(
                    Color.rgb(12, 95, 160),
                    Color.rgb(20, 45, 100)
                )
            ).apply {
                cornerRadius = dp(18).toFloat()
                setStroke(dp(1), cyan)
            }
        }
    }

    private fun layout(): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(24), dp(22), dp(18))
            setBackgroundColor(bg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = bg
        window.navigationBarColor = bg

        showHome()
    }

    private fun showHome() {
        root = layout()

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        header.addView(
            text("E D I T H", 25f, cyan, true),
            LinearLayout.LayoutParams(0, dp(48), 1f)
        )

        header.addView(
            text("● SYSTEM ONLINE", 10f, cyan, true),
            LinearLayout.LayoutParams(0, dp(48), 1f)
        )

        root.addView(header)

        root.addView(
            text("EVEN DEAD, I'M THE HERO", 10f, Color.GRAY),
            LinearLayout.LayoutParams(-1, dp(30))
        )

        root.addView(
            OrbView(),
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        root.addView(
            text("HELLO, I AM EDITH", 21f, white, true),
            LinearLayout.LayoutParams(-1, dp(42))
        )

        root.addView(
            text("YOUR PERSONAL DIGITAL ASSISTANT", 10f, cyan),
            LinearLayout.LayoutParams(-1, dp(30))
        )

        val activate = button("ACTIVATE EDITH")

        root.addView(
            activate,
            LinearLayout.LayoutParams(-1, dp(58)).apply {
                topMargin = dp(14)
            }
        )

        activate.setOnClickListener {
            showLive()
        }

        root.addView(
            text("EDITH 3.1  /  LIVE INTERFACE", 10f, Color.LTGRAY),
            LinearLayout.LayoutParams(-1, dp(42))
        )

        setContentView(root)
    }

    private fun showLive() {
        root = layout()

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val back = button("‹")
        header.addView(back, LinearLayout.LayoutParams(dp(55), dp(48)))
        back.setOnClickListener { showHome() }

        header.addView(
            text("EDITH  /  LIVE", 18f, cyan, true),
            LinearLayout.LayoutParams(0, dp(48), 1f)
        )

        root.addView(header)

        root.addView(
            text("REAL-TIME VOICE INTERFACE", 10f, Color.GRAY),
            LinearLayout.LayoutParams(-1, dp(35))
        )

        statusText = text(
            "LIVE ENGINE NOT CONNECTED",
            11f,
            cyan,
            true
        )

        root.addView(
            statusText,
            LinearLayout.LayoutParams(-1, dp(35))
        )

        root.addView(
            OrbView(),
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        root.addView(
            text("EDITH IS READY", 21f, white, true),
            LinearLayout.LayoutParams(-1, dp(45))
        )

        root.addView(
            text(
                "Voice connection will be available soon",
                11f,
                Color.LTGRAY
            ),
            LinearLayout.LayoutParams(-1, dp(35))
        )

        val mic = button("🎙  VOICE CONNECTION")
        root.addView(
            mic,
            LinearLayout.LayoutParams(-1, dp(58)).apply {
                topMargin = dp(12)
            }
        )

        mic.setOnClickListener {
            Toast.makeText(
                this,
                "Live voice engine will be connected in the next version.",
                Toast.LENGTH_LONG
            ).show()
        }

        val end = button("END SESSION")
        root.addView(
            end,
            LinearLayout.LayoutParams(-1, dp(52)).apply {
                topMargin = dp(10)
            }
        )

        end.setOnClickListener {
            showHome()
        }

        root.addView(
            text("EDITH 3.1  /  VOICE UI", 10f, Color.GRAY),
            LinearLayout.LayoutParams(-1, dp(35))
        )

        setContentView(root)
    }

    inner class OrbView : View(this@MainActivity) {

        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private var rotation = 0f

        private val animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 12000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                rotation = it.animatedValue as Float
                invalidate()
            }
        }

        init {
            animator.start()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val cx = width / 2f
            val cy = height / 2f
            val radius = minOf(width, height) * 0.25f

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = dp(1).toFloat()
            paint.color = Color.rgb(25, 100, 160)

            canvas.drawCircle(cx, cy, radius * 1.7f, paint)

            canvas.save()
            canvas.rotate(rotation, cx, cy)

            paint.color = cyan
            paint.strokeWidth = dp(2).toFloat()

            canvas.drawArc(
                cx - radius * 1.9f,
                cy - radius * 1.9f,
                cx + radius * 1.9f,
                cy + radius * 1.9f,
                20f, 95f, false, paint
            )

            canvas.drawArc(
                cx - radius * 1.9f,
                cy - radius * 1.9f,
                cx + radius * 1.9f,
                cy + radius * 1.9f,
                200f, 65f, false, paint
            )

            canvas.restore()

            paint.style = Paint.Style.FILL
            paint.shader = RadialGradient(
                cx, cy, radius,
                intArrayOf(
                    Color.WHITE,
                    cyan,
                    Color.rgb(15, 80, 180),
                    Color.TRANSPARENT
                ),
                null,
                Shader.TileMode.CLAMP
            )

            canvas.drawCircle(cx, cy, radius, paint)
            paint.shader = null

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = dp(1).toFloat()
            paint.color = white
            canvas.drawCircle(cx, cy, radius * 1.12f, paint)

            paint.color = cyan
            canvas.drawCircle(cx, cy, radius * 1.4f, paint)

            paint.style = Paint.Style.FILL
            paint.color = white
            paint.textSize = dp(12).toFloat()
            paint.textAlign = Paint.Align.CENTER

            canvas.drawText(
                "E D I T H",
                cx,
                cy + radius * 2.1f,
                paint
            )
        }

        override fun onDetachedFromWindow() {
            animator.cancel()
            super.onDetachedFromWindow()
        }
    }
}
