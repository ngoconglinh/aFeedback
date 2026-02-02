package com.ice.feedback

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ice.feedback.databinding.ActivityMainBinding
import com.lu.feedback.FeedbackDialog
import com.lu.feedback.model.Feedback

class MainActivity : AppCompatActivity() {
    private lateinit var bd: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bd.root)
        ViewCompat.setOnApplyWindowInsetsListener(bd.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fbItem = listOf(
            Feedback(text = "🗑️ Can’t recover messages", textNonTranslate = "Can’t recover messages"),
            Feedback(text = "🔐 Login issues", textNonTranslate = "Login issues"),
            Feedback(text = "⏸️ Status saver not working", textNonTranslate = "Status saver not working"),
            Feedback(text = "📁 Downloaded files not found", textNonTranslate = "Downloaded files not found"),
            Feedback(text = "🤔 Hard to use", textNonTranslate = "Hard to use"),
            Feedback(text = "💥 App crashes or lags", textNonTranslate = "App crashes or lags"),
            Feedback(text = "📢 Too many ads", textNonTranslate = "Too many ads"),
        )

        val dialog = FeedbackDialog.Builder(this)
            .addFeedbackItem(fbItem)
            .appName("abc")
            .versionName("V123")
            .setSenderEmail("feedback@lutech.ltd")
            .setSenderPass("abc")
            .setReceiveEmail("universe@lutech.ltd")
            .setColorTheme(Color.RED)
            .setOtherText("Others")
            .addListener(object: FeedbackDialog.FeedbackListener {
                override fun onRate() {
                }

                override fun onFeedback() {
                }
            })
            .build()

        bd.btnShow.setOnClickListener {

            dialog.showDialog(isOpenFeedback = false)
        }
        bd.btnUiMode.setOnClickListener {
            it.isSelected = !it.isSelected
            enableDarkMode(it.isSelected)
        }
    }


    private fun enableDarkMode(enable: Boolean) {
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        val newNightMode = if (enable) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        if (currentNightMode != newNightMode) {
            AppCompatDelegate.setDefaultNightMode(newNightMode)
        }
    }
}