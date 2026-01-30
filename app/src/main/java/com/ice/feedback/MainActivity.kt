package com.ice.feedback

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
            Feedback(text = "🗑️ Can’t recover messages", textNonTranslate = "AAAAAAAAAAAAAAAAAAA"),
            Feedback(text = "🔐 Login issues", textNonTranslate = "BBBBBBBBBBBBBBBBBBB"),
            Feedback(text = "⏸️ Status saver not working", textNonTranslate = "CCCCCCCCCCCCCCCCCCC"),
            Feedback(text = "📁 Downloaded files not found", textNonTranslate = "DDDDDDDDDDDDDDDDDDD"),
            Feedback(text = "🤔 Hard to use", textNonTranslate = "EEEEEEEEEEEEEEEEEEE"),
            Feedback(text = "💥 App crashes or lags", textNonTranslate = "FFFFFFFFFFFFFFFFFFF"),
            Feedback(text = "📢 Too many ads", textNonTranslate = "GGGGGGGGGGGGGGGGGGG"),
        )

        val dialog = FeedbackDialog.Builder(this)
            .addFeedbackItem(fbItem)
            .appName("abc")
            .versionName("V123")
            .setSenderEmail("feedback@lutech.ltd")
            .setSenderPass("abc")
            .setReceiveEmail("universe@lutech.ltd")
            .setColorTheme(Color.RED)
            .setOtherText("GGGGGGGGGGGGGGGGGGG")
            .addListener(object: FeedbackDialog.FeedbackListener {
                override fun onRate() {
                    TODO("Not yet implemented")
                }

                override fun onFeedback() {
                    TODO("Not yet implemented")
                }
            })
            .build()

        bd.btnShow.setOnClickListener {

            dialog.showDialog(isOpenFeedback = true)
        }
    }
}