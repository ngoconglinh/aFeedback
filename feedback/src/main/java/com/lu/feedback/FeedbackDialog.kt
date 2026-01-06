package com.lu.feedback

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lu.feedback.Utils.goToCHPlay
import com.lu.feedback.databinding.FeedbackMainBinding
import com.lu.feedback.model.Feedback
import com.lu.feedback.widget.FeedbackViewListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedbackDialog(
    private val activity: Activity
) : Dialog(activity) {

    private var viewBinding: FeedbackMainBinding? = null
    internal var listFeedback = listOf<Feedback>()
    internal var appName = ""
    internal var versionName = ""
    internal var senderEmail = ""
    internal var senderPass = ""
    internal var receiveEmail = ""

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        viewBinding = FeedbackMainBinding.inflate(LayoutInflater.from(activity))
        setContentView(viewBinding!!.root)

        window?.let { window ->
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.setDimAmount(0f)

            val layoutParams = window.attributes
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = layoutParams
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding?.fbView?.setPrimaryColor(Color.BLUE)
        viewBinding?.fbView?.submitFeedbackItem(listFeedback)
        viewBinding?.fbView?.addListener(object : FeedbackViewListener {
            override fun onEndFeedback() {
                dismiss()
            }

            override fun onOpenStoreForRate() {
                dismiss()
                goToCHPlay(activity)
            }

            override fun onCloseClicked() {
                dismiss()
            }

            override fun onSendFeedback(list: List<String>, otherText: String) {
                onSendFeedBack(list, otherText)
            }
        })
        setOnShowListener {
            viewBinding?.fbView?.onStart()
        }
        setOnDismissListener {
            viewBinding?.fbView?.resetView()
        }
    }

    private fun onSendFeedBack(list: List<String>, otherText: String) {

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val subject = "Feedback ($format): $appName. Version - $versionName"

        val txtOutput = list.toString() + "\n\n" +
                "" + otherText + "\n\n" +
                "---DEVICE INFO---" + "\n" +
                "MODEL: " + Build.MODEL + "\n" +
                "ID: " + Build.ID + "\n" +
                "Manufacture: " + Build.MANUFACTURER + "\n" +
                "brand: " + Build.BRAND + "\n" +
                "type: " + Build.TYPE + "\n" +
                "user: " + Build.USER + "\n" +
                "BASE: " + Build.VERSION_CODES.BASE + "\n" +
                "INCREMENTAL " + Build.VERSION.INCREMENTAL + "\n" +
                "SDK :" + Build.VERSION.SDK_INT + "\n" +
                "BOARD: " + Build.BOARD + "\n" +
                "BRAND " + Build.BRAND + "\n" +
                "HOST :" + Build.HOST + "\n" +
                "FINGERPRINT: " + Build.FINGERPRINT + "\n" +
                "Version Code: " + Build.VERSION.RELEASE

        CoroutineScope(Dispatchers.IO).launch {
            SendMail(senderEmail, senderPass)
                .send(receiveEmail, subject, txtOutput)
        }
    }

    override fun show() {
        if (!activity.isDestroyed && !activity.isFinishing) {
            super.show()
        }
    }

    override fun dismiss() {
        if (!activity.isDestroyed && !activity.isFinishing) {
            super.dismiss()
        }
    }

    class Builder(private val activity: Activity) {
        private var fbItem: List<Feedback>? = null
        private var appName: String? = null
        private var versionName: String? = null
        private var senderEmail: String? = null
        private var senderPass: String? = null
        private var receiveEmail: String? = null


        fun addFeedbackItem(fbItem: List<Feedback>): Builder {
            this.fbItem = fbItem
            return this
        }

        fun appName(name: String): Builder {
            this.appName = name
            return this
        }

        fun versionName(name: String): Builder {
            this.versionName = name
            return this
        }

        fun setSenderEmail(senderEmail: String): Builder {
            this.senderEmail = senderEmail
            return this
        }

        fun setSenderPass(senderPass: String): Builder {
            this.senderPass = senderPass
            return this
        }

        fun setReceiveEmail(receiveEmail: String): Builder {
            this.receiveEmail = receiveEmail
            return this
        }

        fun build(): FeedbackDialog {
            if (fbItem == null) {
                throw IllegalStateException("Feedback items are required. Please call addFeedbackItem().")
            }
            if (appName == null) {
                throw IllegalStateException("Feedback items are required. Please call appName().")
            }
            if (versionName == null) {
                throw IllegalStateException("Feedback items are required. Please call versionName().")
            }
            if (senderEmail == null) {
                throw IllegalStateException("Feedback items are required. Please call setSenderEmail().")
            }
            if (senderPass == null) {
                throw IllegalStateException("Feedback items are required. Please call setSenderPass().")
            }
            if (receiveEmail == null) {
                throw IllegalStateException("Feedback items are required. Please call setReceiveEmail().")
            }

            return FeedbackDialog(activity).apply {
                this.listFeedback = this@Builder.fbItem!!
                this.appName = this@Builder.appName!!
                this.versionName = this@Builder.versionName!!
                this.senderEmail = this@Builder.senderEmail!!
                this.senderPass = this@Builder.senderPass!!
                this.receiveEmail = this@Builder.receiveEmail!!
            }
        }
    }

}