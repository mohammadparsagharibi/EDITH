package com.parnix.edith

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import android.speech.RecognizerIntent
import android.content.Intent
import java.util.Locale

class MainActivity : Activity() {

    private lateinit var resultText: TextView
    private lateinit var input: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(3, 12, 30)
        window.navigationBarColor = Color.BLACK

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
            val message = input.text.toString()
            resultText.text = "YOU: $message\n\nEDITH: پیام دریافت شد."
            input.text.clear()
        }

        val voice = Button(this)
        voice.text = "🎙 SPEAK"
        voice.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "fa-IR"
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            try {
                startActivityForResult(intent, 100)
            } catch (e: Exception) {
                resultText.text = "Voice recognition unavailable"
            }
        }

        layout.addView(title)
        layout.addView(resultText)
        layout.addView(input)
        layout.addView(send)
        layout.addView(voice)

        setContentView(layout)
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
                input.setText(matches[0])
            }
        }
    }
}
