
package com.parnix.edith

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.animation.ValueAnimator
import android.widget.*
import java.util.Locale

class MainActivity : Activity() {

    private val bg = Color.rgb(3, 9, 22)
    private val panel = Color.rgb(9, 23, 43)
    private val cyan = Color.rgb(70, 220, 255)
    private val blue = Color.rgb(40, 120, 255)
    private val white = Color.WHITE

    private lateinit var root: LinearLayout
    private lateinit var chatMessages: LinearLayout
    private lateinit var chatScroll: ScrollView
    private lateinit var input: EditText
    private lateinit var tts: TextToSpeech

    private var ttsReady = false
    private var ttsStatus = "VOICE INITIALIZING"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = bg
        window.navigationBarColor = bg

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale("fa", "IR"))
                ttsReady =
                    result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED

                ttsStatus = if (ttsReady) {
                    "VOICE SYSTEM READY"
                } else {
                    "PERSIAN VOICE UNAVAILABLE"
                }
            } else {
                ttsStatus = "VOICE SYSTEM ERROR"
            }
        }

        showHome()
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    private fun makeText(
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

    private fun makeButton(value: String): Button {
        return Button(this).apply {
            text = value
            textSize = 14f
            setTextColor(white)
            isAllCaps = false
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

    private fun baseLayout(): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(24), dp(22), dp(18))
            setBackgroundColor(bg)
        }
    }

    private fun showHome() {
        root = baseLayout()

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val title = makeText("E D I T H", 25f, cyan, true)
        title.gravity = Gravity.CENTER_VERTICAL

        val status = makeText("● SYSTEM ONLINE", 10f, cyan, true)
        status.gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT

        header.addView(title, LinearLayout.LayoutParams(0, dp(48), 1f))
        header.addView(status, LinearLayout.LayoutParams(0, dp(48), 1f))

        root.addView(header)

        root.addView(
            makeText(
                "EVEN DEAD, I'M THE HERO",
                10f,
                Color.rgb(100, 155, 190)
            ),
            LinearLayout.LayoutParams(-1, dp(30))
        )

        val orb = OrbView()
        root.addView(
            orb,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        root.addView(
            makeText("HELLO, I AM EDITH", 21f, white, true),
            LinearLayout.LayoutParams(-1, dp(42))
        )

        root.addView(
            makeText(
                "YOUR PERSONAL DIGITAL ASSISTANT",
                10f,
                cyan
            ),
            LinearLayout.LayoutParams(-1, dp(30))
        )

        val activate = makeButton("ACTIVATE EDITH")
        root.addView(
            activate,
            LinearLayout.LayoutParams(-1, dp(58)).apply {
                topMargin = dp(14)
            }
        )

        activate.setOnClickListener {
            showChat()
        }

        root.addView(
            makeText("VOICE: $ttsStatus", 10f, Color.LTGRAY),
            LinearLayout.LayoutParams(-1, dp(42))
        )

        setContentView(root)
    }

    private fun showChat() {
        root = baseLayout()

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val back = makeButton("‹")
        header.addView(back, LinearLayout.LayoutParams(dp(55), dp(48)))
        back.setOnClickListener { showHome() }

        val heading = makeText("EDITH  /  CHAT", 18f, cyan, true)
        header.addView(
            heading,
            LinearLayout.LayoutParams(0, dp(48), 1f)
        )

        root.addView(header)

        root.addView(
            makeText("SECURE COMMUNICATION CHANNEL", 10f, Color.GRAY),
            LinearLayout.LayoutParams(-1, dp(32))
        )

        chatScroll = ScrollView(this)
        chatMessages = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(12), 0, dp(12))
        }

        chatScroll.addView(chatMessages)

        root.addView(
            chatScroll,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        addMessage("EDITH", "سلام! من EDITH هستم. چطور می‌تونم کمکت کنم؟", false)

        val bottom = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        input = EditText(this).apply {
            hint = "پیامت رو بنویس..."
            setTextColor(white)
            setHintTextColor(Color.GRAY)
            textSize = 14f
            singleLine = true
            background = GradientDrawable().apply {
                setColor(panel)
                cornerRadius = dp(15).toFloat()
                setStroke(dp(1), Color.rgb(35, 105, 145))
            }
            setPadding(dp(12), 0, dp(12), 0)
        }

        val send = makeButton("➤")
        val voice = makeButton("🎙")

        bottom.addView(
            input,
            LinearLayout.LayoutParams(0, dp(52), 1f)
        )

        bottom.addView(
            voice,
            LinearLayout.LayoutParams(dp(55), dp(52)).apply {
                leftMargin = dp(5)
            }
        )

        bottom.addView(
            send,
            LinearLayout.LayoutParams(dp(55), dp(52)).apply {
                leftMargin = dp(5)
            }
        )

        root.addView(
            bottom,
            LinearLayout.LayoutParams(-1, dp(58)).apply {
                topMargin = dp(8)
            }
        )

        send.setOnClickListener {
            sendMessage(input.text.toString())
        }

        voice.setOnClickListener {
            startVoiceInput()
        }

        setContentView(root)
    }

    private fun addMessage(
        sender: String,
        message: String,
        isUser: Boolean
    ) {
        val bubble = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14), dp(10), dp(14), dp(10))
            background = GradientDrawable().apply {
                setColor(
                    if (isUser) Color.rgb(15, 48, 80)
                    else Color.rgb(10, 29, 49)
                )
                cornerRadius = dp(16).toFloat()
                setStroke(
                    dp(1),
                    if (isUser) blue else Color.rgb(35, 130, 165)
                )
            }
        }

        bubble.addView(
            makeText(
                sender,
                10f,
                if (isUser) cyan else Color.rgb(90, 210, 255),
                true
            ).apply {
                gravity = Gravity.LEFT
            }
        )

        bubble.addView(
            TextView(this).apply {
                text = message
                textSize = 15f
                setTextColor(white)
                gravity = Gravity.RIGHT
                setPadding(0, dp(5), 0, 0)
            }
        )

        val params = LinearLayout.LayoutParams(-1, -2).apply {
            bottomMargin = dp(12)
        }

        chatMessages.addView(bubble, params)

        chatScroll.post {
            chatScroll.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun sendMessage(message: String) {
        if (message.isBlank()) return

        addMessage("YOU", message, true)
        input.text.clear()

        val reply =
            "پیامت رو دریافت کردم. هنوز هوش مصنوعی واقعی به من وصل نشده است."

        addMessage("EDITH", reply, false)
        speak(reply)
    }

    private fun speak(message: String) {
        if (!ttsReady) {
            Toast.makeText(
                this,
                "صدای فارسی گوشی آماده نیست.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        tts.speak(
            message,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "EDITH_VOICE"
        )
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            "fa-IR"
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            "با EDITH صحبت کنید..."
        )

        try {
            startActivityForResult(intent, 100)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "تشخیص صدا در دسترس نیست.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val matches = data?.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            )

            if (!matches.isNullOrEmpty()) {
                val spoken = matches[0]
                    .replace("ي", "ی")
                    .replace("ك", "ک")

                input.setText(spoken)
                sendMessage(spoken)
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
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
            val radius = minOf(width, height) * 0.29f

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = dp(1).toFloat()
            paint.color = Color.rgb(25, 100, 160)
            canvas.drawCircle(cx, cy, radius * 1.55f, paint)

            paint.color = cyan
            paint.strokeWidth = dp(2).toFloat()
            canvas.save()
            canvas.rotate(rotation, cx, cy)

            canvas.drawArc(
                cx - radius * 1.8f,
                cy - radius * 1.8f,
                cx + radius * 1.8f,
                cy + radius * 1.8f,
                20f, 95f, false, paint
            )

            canvas.drawArc(
                cx - radius * 1.8f,
                cy - radius * 1.8f,
                cx + radius * 1.8f,
                cy + radius * 1.8f,
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
            paint.color = Color.WHITE
            canvas.drawCircle(cx, cy, radius * 1.12f, paint)

            paint.color = cyan
            canvas.drawCircle(cx, cy, radius * 1.35f, paint)

            paint.style = Paint.Style.FILL
            paint.color = white
            paint.textSize = dp(12).toFloat()
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(
                "E D I T H",
                cx,
                cy + radius * 1.85f,
                paint
            )
        }

        override fun onDetachedFromWindow() {
            animator.cancel()
            super.onDetachedFromWindow()
        }
    }
}
