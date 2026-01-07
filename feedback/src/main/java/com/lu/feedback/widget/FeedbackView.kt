package com.lu.feedback.widget

import android.animation.Animator
import android.content.Context
import android.content.res.ColorStateList
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.graphics.toColorInt
import androidx.core.widget.addTextChangedListener
import com.lu.feedback.R
import com.lu.feedback.Utils
import com.lu.feedback.Utils.enable
import com.lu.feedback.adapter.FeedbackAdapter
import com.lu.feedback.databinding.FeedbackViewBinding
import com.lu.feedback.model.Feedback
import com.lu.feedback.model.Feedback.Companion.getOtherFb

class FeedbackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val viewBinding = FeedbackViewBinding.inflate(LayoutInflater.from(context), this, true)

    private var starBtn = listOf<ImageView>()
    private var feedbackViewListener: FeedbackViewListener? = null
    private var starCount = 0
    private var feedbackString = listOf<Feedback>()

    private val fbAdapter = FeedbackAdapter {
        feedbackString = it
        sendFeedBackEnable()
    }

    fun addListener(listener: FeedbackViewListener) {
        this.feedbackViewListener = listener
    }

    fun onStart(isShowFeedback: Boolean = false){
        if (isShowFeedback) {
            showFeedback()
        } else {
            showRating()
            viewBinding.icRate.lottieView.playAnimation()
        }
    }

    fun resetView() {
        starCount = 0
        onStarRatingChange(0)
        feedbackString = emptyList()
        sendFeedBackEnable()
        val currentFb = fbAdapter.currentList.filterNotNull()
        val newList = currentFb.map {
            it.copy(isSelected = false)
        }
        fbAdapter.submitList(newList)
        viewBinding.icRate.lottieView.visibility = VISIBLE
        viewBinding.icRate.llStar.visibility = INVISIBLE
        showRating()
    }

    fun submitFeedbackItem(list: List<Feedback>) {
        val dataWithOther = list.toMutableList().apply {
            add(getOtherFb(context))
        }
        fbAdapter.submitList(dataWithOther)
    }

    fun setPrimaryColor(color: Int) {
        viewBinding.apply {
            icRate.tvRate.background.setTint(color)
            icThanks.iconThanks.background.setTint(color)
            icFB.tvSend.background.setTint(color)
            icThanks.tvThanksForFeedback.setTextColor(color)

            val states = arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked),
            )

            val colors = intArrayOf(color, "#BFBFBF".toColorInt())

            val stateList = ColorStateList(states, colors)
            fbAdapter.iconColorStateList = stateList
        }
    }

    init {
        initView()
        initEvent()
        onStarRatingChange(0)
    }

    private fun initView() = with(viewBinding) {
        starBtn = listOf(icRate.ivStar1, icRate.ivStar2, icRate.ivStar3, icRate.ivStar4, icRate.ivStar5)
        icFB.rcvFb.adapter = fbAdapter
    }

    private fun initEvent() = with(viewBinding) {
        root.setOnClickListener {
            feedbackViewListener?.onCloseClicked()
        }
        starBtn.forEachIndexed { i, view ->
            view.setOnClickListener {
                icRate.lottieView.visibility = INVISIBLE
                onStarRatingChange(i + 1)
            }
        }

        icRate.apply {
            lottieView.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {}
                override fun onAnimationEnd(p0: Animator) {
                    try {
                        lottieView.visibility = INVISIBLE
                        llStar.visibility = VISIBLE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onAnimationCancel(p0: Animator) {}
                override fun onAnimationRepeat(p0: Animator) {}
            })
            tvRate.setOnClickListener {
                if (starCount >= 4) {
                    feedbackViewListener?.onOpenStoreForRate()
                } else {
                    showFeedback()
                }
            }
            tvLate.setOnClickListener {
                feedbackViewListener?.onCloseClicked()
            }
        }

        icFB.apply {
            tvSend.setOnClickListener {
                feedbackViewListener?.onSendFeedback(feedbackString.map { it.textNonTranslate }, edtFeedback.text.toString())
                showThanks()
            }
            edtFeedback.addTextChangedListener {
                sendFeedBackEnable()
            }
        }
    }

    private fun showRating() = with(viewBinding) {
        icRate.root.visibility = VISIBLE
        icFB.root.visibility = GONE
        icThanks.root.visibility = GONE
    }

    fun showFeedback() = with(viewBinding) {
        icRate.root.visibility = GONE
        icFB.root.visibility = VISIBLE
        icThanks.root.visibility = GONE
    }

    private fun showThanks() = with(viewBinding) {
        icRate.root.visibility = GONE
        icFB.root.visibility = GONE
        icThanks.root.visibility = VISIBLE
        Utils.hideKeyboard(root)
        timer.start()
    }

    private fun onStarRatingChange(star: Int) {
        this.starCount = star
        starBtn.forEachIndexed { i, imageView ->
            imageView.isSelected = i < star
        }
        viewBinding.icRate.apply {
            val (title, description, emojiResource) = when (star) {
                4, 5 -> {
                    Triple(
                        R.string.txt_thank_you_for_the_love,
                        R.string.txt_your_rating_made_our_day,
                        R.drawable.ic_rating_emoji_love
                    )
                }

                0 -> {
                    Triple(
                        R.string.txt_enjoying_the_app,
                        R.string.txt_your_feedback_helps_us_get_better,
                        R.drawable.ic_rating_emoji_nor
                    )
                }

                else -> {
                    Triple(
                        R.string.txt_not_enjoying_the_app,
                        R.string.txt_could_you_share_what_went_wrong,
                        R.drawable.ic_rating_emoji_sad
                    )
                }
            }
            tvEnjoy.text = context.getString(title)
            tvRateDescription.text = context.getString(description)
            ivRateEmoji.setImageResource(emojiResource)
            tvRate.enable(star > 0)
        }
    }

    private val timer: CountDownTimer = object : CountDownTimer(2500L, 2500L) {
        override fun onTick(p0: Long) {

        }

        override fun onFinish() {
            feedbackViewListener?.onEndFeedback()
        }
    }

    private fun sendFeedBackEnable() {
        val b1 = feedbackString.isNotEmpty()
        val b2 = if (feedbackString.any { it.isOtherItem }) {
            viewBinding.icFB.edtFeedback.text.toString().replace(" ", "") != ""
        } else {
            true
        }
        viewBinding.icFB.tvSend.enable(b1 && b2)
    }

    override fun onDetachedFromWindow() {
        timer.cancel()
        super.onDetachedFromWindow()
    }

}