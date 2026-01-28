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
            Feedback(text = "AAAAAAAAAAAAAAAAAAA", textNonTranslate = "AAAAAAAAAAAAAAAAAAA"),
            Feedback(text = "BBBBBBBBBBBBBBBBBBB", textNonTranslate = "BBBBBBBBBBBBBBBBBBB"),
            Feedback(text = "CCCCCCCCCCCCCCCCCCC", textNonTranslate = "CCCCCCCCCCCCCCCCCCC"),
            Feedback(text = "DDDDDDDDDDDDDDDDDDD", textNonTranslate = "DDDDDDDDDDDDDDDDDDD"),
            Feedback(text = "EEEEEEEEEEEEEEEEEEE", textNonTranslate = "EEEEEEEEEEEEEEEEEEE"),
            Feedback(text = "FFFFFFFFFFFFFFFFFFF", textNonTranslate = "FFFFFFFFFFFFFFFFFFF"),
            Feedback(text = "GGGGGGGGGGGGGGGGGGG", textNonTranslate = "GGGGGGGGGGGGGGGGGGG"),
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
            .build()

        bd.btnShow.setOnClickListener {

            dialog.showDialog(isOpenFeedback = true)
        }
    }
}