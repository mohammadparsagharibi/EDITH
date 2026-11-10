
package com.parnix.edith

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import android.speech.RecognizerIntent
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class MainActivity : Activity() {

    private lateinit var resultText: TextView
    private lateinit var input: EditText
    private lateinit var tts: TextToSpeech
    private var ttsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(3, 12, 30)
        window.navigationBarColor = Color.BLACK

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale("fa", "IR")
                ttsReady = true
            }
        }

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(24, 40, 24, 24)
        layout.setBackgroundColor(Color.rgb(3, 12, 30))

        val title = TextView(this)
        title.text = "E D I T H"
        title.textSize = 32f
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.CYAN)

        resultText = TextView(this)
        resultText.text = "SYSTEM ONLINE\n\nسلام! من EDITH هستم."
        resultText.textSize = 18f
        resultText.setTextColor(Color.WHITE)
        resultText.setPadding(8, 50, 8, 50)

        input = EditText(this)
        input.hint = "پیامت رو بنویس..."
        input.setTextColor(Color.WHITE)
        input.setHintTextColor(Color.GRAY)

        val send = Button(this)
        send.text = "SEND"
        send.setOnClickListener {
            sendMessage(input.text.toString())
        }

        val voice = Button(this)
        voice.text = "🎙 SPEAK"
        voice.setOnClickListener {
            startVoiceInput()
        }

        layout.addView(title)
        layout.addView(resultText)
        layout.addView(input)
        layout.addView(send)
        layout.addView(voice)

        setContentView(layout)
    }

    private fun sendMessage(message: String) {
        if (message.isBlank()) return

        val reply = "پیامت رو دریافت کردم. هنوز هوش مصنوعی به من وصل نشده است."

        resultText.text = "YOU: $message\n\nEDITH: $reply"
        input.text.clear()
        speak(reply)
    }

    private fun speak(message: String, after: (() -> Unit)? = null) {
        if (!ttsReady) {
            resultText.text = "موتور صدای گوشی هنوز آماده نیست."
            return
        }

        val id = System.currentTimeMillis().toString()

        if (after != null) {
            tts.setOnUtteranceProgressListener(
                object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}

                    override fun onDone(utteranceId: String?) {
                        runOnUiThread { after() }
                    }

                    override fun onError(utteranceId: String?) {}
                }
            )
        }

        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, id)
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
            "بگو سلام ادیث..."
        )

        try {
            startActivityForResult(intent, 100)
        } catch (e: Exception) {
            resultText.text = "تشخیص گفتار در دسترس نیست."
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
                val normalized = spoken.replace("ي", "ی")
                    .replace("ك", "ک")

                if (
                    normalized.contains("سلام ادیث") ||
                    normalized.contains("سلام ایدث")
                ) {
                    resultText.text = "EDITH: در خدمتم. بفرمایید."
                    speak("در خدمتم. بفرمایید.") {
                        startVoiceInput()
                    }
                } else {
                    sendMessage(spoken)
                }
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
}
