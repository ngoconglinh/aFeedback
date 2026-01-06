package com.ice.feedback

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
            Feedback("AAAAAAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAAAAAA"),
            Feedback("BBBBBBBBBBBBBBBBBBB", "BBBBBBBBBBBBBBBBBBB"),
            Feedback("CCCCCCCCCCCCCCCCCCC", "CCCCCCCCCCCCCCCCCCC"),
            Feedback("DDDDDDDDDDDDDDDDDDD", "DDDDDDDDDDDDDDDDDDD"),
            Feedback("EEEEEEEEEEEEEEEEEEE", "EEEEEEEEEEEEEEEEEEE"),
            Feedback("FFFFFFFFFFFFFFFFFFF", "FFFFFFFFFFFFFFFFFFF"),
            Feedback("GGGGGGGGGGGGGGGGGGG", "GGGGGGGGGGGGGGGGGGG"),
        )
        val dialog = FeedbackDialog.Builder(this)
            .addFeedbackItem(fbItem)
            .appName("aanbc")
            .versionName("V123")
            .setSenderEmail("feedback@lutech.ltd")
            .setSenderPass("hipq dptz sisv dkca")
            .setReceiveEmail("universe@lutech.ltd")
            .build()

        bd.btnShow.setOnClickListener {
            dialog.show()
        }
    }
}